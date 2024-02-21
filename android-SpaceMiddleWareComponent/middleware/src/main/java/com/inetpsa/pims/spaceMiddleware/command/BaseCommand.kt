package com.inetpsa.pims.spaceMiddleware.command

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.commandManager.Command
import com.inetpsa.mmx.foundation.extensions.asMap
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor
import com.inetpsa.pims.spaceMiddleware.util.asJson
import com.inetpsa.pims.spaceMiddleware.util.asMap
import com.inetpsa.pims.spaceMiddleware.util.handleResult

internal abstract class BaseCommand : Command() {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal val actionType: String
        @Throws(PIMSError::class)
        get() = parameters has Constants.Input.ACTION_TYPE

    val middlewareComponent: MiddlewareComponent
        @Throws(PIMSError::class)
        get() = componentReference?.get() as? MiddlewareComponent
            ?: throw PIMSFoundationError.componentNotConfigured(Constants.COMPONENT_NAME)

    override suspend fun execute(callback: () -> Unit) {
        try {
            getExecutor().execute().handleResult(
                actionSuccess = { result ->
                    val response = when (result) {
                        null, is Unit -> emptyMap<String, String>()
                        else -> result.asJson().asMap()
                    }
                    success(response)
                    callback()
                },
                actionFailure = {
                    failure(it.asMap())
                    callback()
                }
            )
        } catch (e: PIMSError) {
            PIMSLogger.w(e)
            failure(e.asMap())
            callback()
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal abstract suspend fun getExecutor(): BaseLocalExecutor<*, *>
}
