package com.inetpsa.pims.spaceMiddleware.executor

import android.net.Uri
import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.communication.ICommunicationManager
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.networkManager.NetworkRequest
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import java.lang.reflect.Type

internal abstract class BaseExecutor<P, out R> constructor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseLocalExecutor<P, R>(middlewareComponent, params) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal abstract val communicationManager: ICommunicationManager

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Throws(PIMSError::class)
    internal abstract fun baseUrl(args: Array<String>): String

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Throws(PIMSError::class)
    internal abstract fun baseHeaders(): Map<String, String>

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Throws(PIMSError::class)
    internal abstract fun baseQueries(): Map<String, String>

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal fun request(
        type: Type,
        urls: Array<String>,
        headers: Map<String, String>? = null,
        queries: Map<String, String>? = null,
        body: String? = null
    ): NetworkRequest {
        val url = baseUrl(urls)
        return NetworkRequest(
            type = type,
            url = Uri.parse(url),
            headers = baseHeaders().toMutableMap().apply { headers?.also { putAll(it) } },
            body = body,
            queries = baseQueries().toMutableMap().apply { queries?.also { putAll(it) } }
        )
    }
}
