package com.inetpsa.pims.spaceMiddleware.executor

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.manager.ConfigurationManager

internal abstract class BaseLocalExecutor<P, out R> constructor(
    protected val middlewareComponent: MiddlewareComponent,
    protected val params: Map<String, Any?>? = null
) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal val configurationManager: ConfigurationManager
        get() = middlewareComponent.configurationManager

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Throws(PIMSError::class)
    internal abstract fun params(parameters: Map<String, Any?>? = params): P

    abstract suspend fun execute(input: P = params()): NetworkResponse<R>
}
