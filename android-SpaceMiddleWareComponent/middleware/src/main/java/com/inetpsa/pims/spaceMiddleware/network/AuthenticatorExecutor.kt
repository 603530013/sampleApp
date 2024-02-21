package com.inetpsa.pims.spaceMiddleware.network

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.mmx.foundation.userSession.model.TokenResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.token.GetMymAccessTokenPsaExecutor
import com.inetpsa.pims.spaceMiddleware.util.unwrap
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import kotlin.coroutines.resume

internal class AuthenticatorExecutor(private val middlewareComponent: MiddlewareComponent) : Authenticator {

    /**
     * Returns a request that includes a credential to satisfy an authentication challenge in
     * [response]. Returns null if the challenge cannot be satisfied.
     *
     * The route is best effort, it currently may not always be provided even when logically
     * available. It may also not be provided when an authenticator is re-used manually in an
     * application interceptor, such as when implementing client-specific retries.
     */
    override fun authenticate(route: Route?, response: Response): Request? {
        PIMSLogger.d("route: $route, response: $response")
        return authenticateWithMymToken(response) ?: authenticateWithCVSToken(response)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun authenticateWithMymToken(response: Response): Request? =
        extractMymToken(response)
            .also { PIMSLogger.i("extracted MyM Token: $it") }
            ?.let { oldToken ->

                // If the token has changed since the request was made, use the new token.
                getMymRefreshedToken()
                    ?.takeIf { it != oldToken }
                    ?.let { newToken ->

                        // Retry the request with the new token.
                        response.request
                            .newBuilder()
                            .removeHeader(Constants.HEADER_PARAM_MYM_TOKEN)
                            .addHeader(Constants.HEADER_PARAM_MYM_TOKEN, newToken)
                            .build()
                    }
            }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun authenticateWithCVSToken(response: Response): Request? =
        extractCVSToken(response)
            .also { PIMSLogger.i("extracted CVS Token: $it") }
            ?.let { oldToken ->
                // If the token has changed since the request was made, use the new token.
                getCVSRefreshedToken()
                    ?.takeIf { it != oldToken }
                    ?.let { newToken ->
                        // Retry the request with the new token.
                        response.request
                            .newBuilder()
                            .removeHeader(Constants.HEADER_PARAM_CVS_TOKEN)
                            .addHeader(Constants.HEADER_PARAM_CVS_TOKEN, newToken)
                            .build()
                    }
            }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun extractMymToken(response: Response): String? =
        response.request.header(Constants.HEADER_PARAM_MYM_TOKEN)?.takeIf { it.isNotBlank() }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun extractCVSToken(response: Response): String? =
        response.request.header(Constants.HEADER_PARAM_CVS_TOKEN)
            ?.takeIf { it.isNotBlank() }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getMymRefreshedToken(): String? =
        runBlocking {
            PIMSLogger.d("")
            try {
                GetMymAccessTokenPsaExecutor(middlewareComponent, emptyMap())
                    .execute()
                    .also { PIMSLogger.i("refresh MYM Token: $it") }
                    .unwrap()
                    .accessToken
                    .takeIf { it.isNotBlank() }
            } catch (ex: PIMSError) {
                PIMSLogger.w(ex)
                null
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getCVSRefreshedToken(): String? =
        runBlocking {
            PIMSLogger.d("")
            try {
                suspendCancellableCoroutine<TokenResponse> {
                    middlewareComponent.userSessionManager.refreshToken(TokenType.CVSToken) { tokenResponse ->
                        it.resume(tokenResponse)
                    }
                }
                    .also { PIMSLogger.i("refresh CVS Token: $it") }
                    .unwrap()
                    .takeIf { it.isNotBlank() }
            } catch (ex: PIMSError) {
                PIMSLogger.w(ex)
                null
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Throws(PIMSError::class)
    internal fun TokenResponse.unwrap(): String = when (this) {
        is TokenResponse.Success -> this.token
        is TokenResponse.Failure -> throw this.error ?: PIMSFoundationError.unknownError
    }
}
