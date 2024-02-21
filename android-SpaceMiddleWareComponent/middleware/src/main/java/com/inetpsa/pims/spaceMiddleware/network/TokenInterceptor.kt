package com.inetpsa.pims.spaceMiddleware.network

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.mmx.foundation.userSession.model.TokenResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import kotlin.coroutines.resume

internal class TokenInterceptor(private val middlewareComponent: MiddlewareComponent) : Interceptor {

    override fun intercept(chain: Chain): Response {
        PIMSLogger.d("")
        val request = chain.request()
        val tokenType = request.header(com.inetpsa.mmx.foundation.tools.Constants.TOKEN_TYPE)
            ?.asTokenType() ?: TokenType.None
        PIMSLogger.d("tokenType: $tokenType")
        val newBuilder = request.newBuilder()
        newBuilder.removeHeader(com.inetpsa.mmx.foundation.tools.Constants.TOKEN_TYPE)
        val newRequest = newBuilder.injectToken(tokenType).build()
        PIMSLogger.d("newRequest: $newRequest")
        return chain.proceed(newRequest)
            .also { PIMSLogger.d("new chain proceed: $it") }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    internal fun Request.Builder.injectToken(tokenType: TokenType): Request.Builder = also { builder ->
        val token = when (val tokenResponse = getToken(tokenType)) {
            is TokenResponse.Success -> tokenResponse.token
            is TokenResponse.Failure -> tokenResponse.error?.message ?: "MyM token is not available"
        }
        PIMSLogger.d("token: $token")

        if (tokenType == MiddlewareCommunicationManager.MymToken) {
            builder.header(Constants.HEADER_PARAM_MYM_TOKEN, token)
        }

        if (tokenType == TokenType.CVSToken) {
            builder.addHeader(Constants.HEADER_PARAM_CVS_TOKEN, token)
        }
    }

    private fun String.asTokenType(): TokenType? =
        when (this) {
            MiddlewareCommunicationManager.MymToken.name -> MiddlewareCommunicationManager.MymToken
            TokenType.CVSToken.name -> TokenType.CVSToken
            else -> null
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getToken(tokenType: TokenType): TokenResponse = runBlocking {
        suspendCancellableCoroutine<TokenResponse> { cont ->
            middlewareComponent.userSessionManager
                .getToken(tokenType) { response ->
                    cont.resume(response)
                }
        }.also { PIMSLogger.d("TokenResponse: $it") }
    }
}
