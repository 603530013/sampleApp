package com.inetpsa.pims.spaceMiddleware.network

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.communication.ICommunicationManager
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkRequest
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import okhttp3.OkHttpClient

internal class MiddlewareCommunicationManager(
    private val middlewareComponent: MiddlewareComponent,
    environment: Environment
) : ICommunicationManager by middlewareComponent.communicationManager {

    companion object {

        val MymToken = TokenType.External(Constants.STORAGE_KEY_MYM_TOKEN)
    }

    private val requestFactory = OkHttpRequestFactory()
    private val client: OkHttpClient = MiddlewareOkHttpClient().build(middlewareComponent, environment)
    private val networkExecutor = MiddlewareNetworkExecutor(client)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun <T> execute(
        method: String,
        request: NetworkRequest,
        tokenType: TokenType
    ): NetworkResponse<T> =
        when (tokenType) {
            MymToken, TokenType.CVSToken -> executeWithMymClient(method, request, tokenType)

            else -> executeWithFoundationClient(method, request, tokenType)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun <T> executeWithFoundationClient(
        method: String,
        request: NetworkRequest,
        tokenType: TokenType
    ): NetworkResponse<T> =
        when {
            method.equals("DELETE", true) ->
                middlewareComponent.communicationManager.delete(request, tokenType)
            method.equals("GET", true) ->
                middlewareComponent.communicationManager.get(request, tokenType)
            method.equals("PATCH", true) ->
                middlewareComponent.communicationManager.patch(request, tokenType)
            method.equals("POST", true) ->
                middlewareComponent.communicationManager.post(request, tokenType)
            method.equals("PUT", true) ->
                middlewareComponent.communicationManager.update(request, tokenType)
            else -> NetworkResponse.Failure(null)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun <T> executeWithMymClient(
        method: String,
        request: NetworkRequest,
        tokenType: TokenType
    ): NetworkResponse<T> {
        PIMSLogger.d("method: $method, request: $request")
        val builder = requestFactory.generateRequest(method, request, tokenType) ?: return NetworkResponse.Failure(null)
        return networkExecutor.execute(builder.build(), request.type)
    }

    override suspend fun <T> delete(request: NetworkRequest, tokenType: TokenType): NetworkResponse<T> =
        execute("DELETE", request, tokenType)

    override suspend fun <T> get(request: NetworkRequest, tokenType: TokenType): NetworkResponse<T> =
        execute("GET", request, tokenType)

    override suspend fun <T> patch(request: NetworkRequest, tokenType: TokenType): NetworkResponse<T> =
        execute("PATCH", request, tokenType)

    override suspend fun <T> post(request: NetworkRequest, tokenType: TokenType): NetworkResponse<T> =
        execute("POST", request, tokenType)

    override suspend fun <T> update(request: NetworkRequest, tokenType: TokenType): NetworkResponse<T> =
        execute("PUT", request, tokenType)
}
