package com.mobiledrivetech.external.middleware.executor

import androidx.annotation.VisibleForTesting
import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.command.BaseCommand
import com.mobiledrivetech.external.middleware.manager.ConfigurationManager
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.model.Response

internal abstract class BaseLocalExecutor<P, out R> constructor(
    protected val middlewareComponent: MiddlewareComponent,
    protected val params: Map<String, Any?>? = null
) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal val configurationManager: ConfigurationManager
        get() = middlewareComponent.configurationManager

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Throws(MiddleWareError::class)
    internal abstract fun params(parameters: Map<String, Any?>? = params): P

    abstract suspend fun execute(input: P = params()): Response<R>
}
