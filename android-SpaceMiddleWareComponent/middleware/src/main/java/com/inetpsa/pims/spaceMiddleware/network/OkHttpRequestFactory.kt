package com.inetpsa.pims.spaceMiddleware.network

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.EndPoint
import com.inetpsa.mmx.foundation.networkManager.NetworkRequest
import com.inetpsa.mmx.foundation.tools.Constants
import com.inetpsa.mmx.foundation.tools.TokenType
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

internal class OkHttpRequestFactory {

    private val json = "application/json; charset=utf-8".toMediaType()

    fun generateRequest(method: String, request: NetworkRequest, tokenType: TokenType): Request.Builder? {
        PIMSLogger.d("method: $method, request: $request")
        val httpUrlBuilder = handleEndPoint(request) ?: return null
        request.queries
            ?.mapValues { entry -> "${entry.value}" }
            ?.forEach { entry -> httpUrlBuilder.addQueryParameter(entry.key, entry.value) }

        val builder = Request.Builder()
            .method(method, request.body?.toRequestBody(json))
            .url(httpUrlBuilder.build())

        builder.addHeader("Accept", "application/json")

        request.headers?.forEach {
            builder.addHeader(it.key, it.value)
        }

        builder.addHeader(Constants.TOKEN_TYPE, tokenType.name)

        PIMSLogger.i("builder: $builder")
        return builder
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handleEndPoint(request: NetworkRequest) = when (val endPoint = request.endPoint) {
        is EndPoint.Path -> endPoint.value
        is EndPoint.Url -> endPoint.value.toString()
        else -> null
    }?.toHttpUrlOrNull()?.newBuilder()
}
