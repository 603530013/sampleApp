package com.mobiledrivetech.external.middleware.command

import androidx.annotation.VisibleForTesting
import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.executor.BaseLocalExecutor
import com.mobiledrivetech.external.middleware.extensions.asJson
import com.mobiledrivetech.external.middleware.extensions.asMap
import com.mobiledrivetech.external.middleware.extensions.handleResult
import com.mobiledrivetech.external.middleware.extensions.has
import com.mobiledrivetech.external.middleware.foundation.commandManager.Command
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.util.MiddleWareFoundationError

internal abstract class BaseCommand : Command() {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal val actionType: String
        @Throws(MiddleWareError::class)
        get() = parameters has Constants.Input.ACTION_TYPE

    val middlewareComponent: MiddlewareComponent
        @Throws(MiddleWareError::class)
        get() = componentReference?.get() as? MiddlewareComponent
            ?: throw MiddleWareFoundationError.componentNotConfigured(Constants.COMPONENT_NAME)

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
        } catch (e: MiddleWareError) {
            MDLog.warning(e.toString())
            failure(e.asMap())
            callback()
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal abstract suspend fun getExecutor(): BaseLocalExecutor<*, *>
}
