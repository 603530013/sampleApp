package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.BookingIdField
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.ServiceSchedulerToken
import com.inetpsa.pims.spaceMiddleware.util.ifSuccess
import com.inetpsa.pims.spaceMiddleware.util.transform
import java.net.HttpURLConnection
import java.time.LocalDateTime

/**
 * Help to manage Nafta dealer OSS token
 *
 * @constructor Create empty Nafta token manager
 */
internal class NaftaTokenManager {

    suspend inline fun <T, P : BookingIdField> withOssToken(
        middlewareComponent: MiddlewareComponent,
        input: P,
        action: (input: P, token: String) -> NetworkResponse<T>
    ): NetworkResponse<T> {
        getOssTokenFromCache(input)
            .takeIf { !it.isNullOrBlank() }
            ?.let {
                val response = action(input, it)
                if (!isTokenUnauthorized(response)) {
                    return response
                }
            }

        return refreshedToken(middlewareComponent, input).transform {
            action(input, it.accessToken)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getOssTokenFromCache(input: BookingIdField): String? {
        return BookingOnlineCache.readToken(input)?.let { response ->
            LocalDateTime.now().plusMinutes(2).isBefore(response.expireTime)
                .takeIf { it }
                ?.let { response.token }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun refreshedToken(
        middlewareComponent: MiddlewareComponent,
        input: BookingIdField
    ): NetworkResponse<ServiceSchedulerToken> =
        GetOssTokenFcaExecutor(middlewareComponent, emptyMap())
            .execute(OssTokenInput(input.dealerId))
            .ifSuccess { updateToken(input, it) }
            .also { PIMSLogger.i("refresh Token: $it") }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun <T> isTokenUnauthorized(response: NetworkResponse<T>): Boolean {
        return response is NetworkResponse.Failure &&
            response.error?.subError?.status == HttpURLConnection.HTTP_UNAUTHORIZED
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun updateToken(input: BookingIdField, response: ServiceSchedulerToken) {
        val tokenResponse = OssTokenCache(
            response.accessToken,
            LocalDateTime.now().plusSeconds(response.expiresIn)
        )
        BookingOnlineCache.write(input, tokenResponse)
    }

    fun removeAll() {
        BookingOnlineCache.clear()
    }

    fun remove(input: BookingIdField) {
        BookingOnlineCache.removeToken(input)
    }
}
