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

/**
 * Base command which inherits from [Command]
 */
internal abstract class BaseCommand : Command() {

    /**
     * Get action type
     *
     * @return action type
     * @throws MiddleWareError if action type is not found
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal val actionType: String
        @Throws(MiddleWareError::class)
        get() = parameters has Constants.Input.ACTION_TYPE

    /**
     * Get middleware component
     *
     * @return middleware component
     * @throws MiddleWareError if component is not configured
     */
    val middlewareComponent: MiddlewareComponent
        @Throws(MiddleWareError::class)
        get() = componentReference?.get() as? MiddlewareComponent
            ?: throw MiddleWareFoundationError.componentNotConfigured(Constants.COMPONENT_NAME)

    /**
     * Execute
     *
     * @param callback with callback for execution
     */
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

    /**
     * Get executor
     *
     * @return executor
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal abstract suspend fun getExecutor(): BaseLocalExecutor<*, *>
}
