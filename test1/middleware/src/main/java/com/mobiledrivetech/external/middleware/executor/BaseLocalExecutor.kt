package com.mobiledrivetech.external.middleware.executor

import androidx.annotation.VisibleForTesting
import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.command.BaseCommand
import com.mobiledrivetech.external.middleware.manager.ConfigurationManager
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.model.Response

/**
 * Base local executor
 *
 * @param P with input type
 * @param R with output type
 */
internal abstract class BaseLocalExecutor<P, out R> constructor(
    protected val middlewareComponent: MiddlewareComponent,
    protected val params: Map<String, Any?>? = null
) {

    /**
     * Constructor
     *
     * @param command with [BaseCommand] for execution
     */
    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    /**
     * Get configuration manager
     *
     * @return the configuration manager
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal val configurationManager: ConfigurationManager
        get() = middlewareComponent.configurationManager

    /**
     * Params which is need to be override by child class
     *
     * @param parameters by [Map<String, Any?]
     * @return
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Throws(MiddleWareError::class)
    internal abstract fun params(parameters: Map<String, Any?>? = params): P

    /**
     * Execute which is need to be override by child class
     *
     * @param input which is the return value of [params]
     * @return the response
     */
    abstract suspend fun execute(input: P = params()): Response<R>
}
