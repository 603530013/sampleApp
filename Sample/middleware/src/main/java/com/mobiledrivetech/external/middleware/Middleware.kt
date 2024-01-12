package com.mobiledrivetech.external.middleware

import androidx.annotation.VisibleForTesting

class Middleware : IMiddleware {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val core: MiddlewareComponent by lazy {
        MiddlewareComponent()
    }

    override fun get(
        api: String,
        parameters: Map<String, Any>?,
        callback: (Map<String, Any?>) -> Unit
    ): String = core.get(api, parameters, callback)

    override fun initialize(
        parameters: Map<String, Any>,
        callback: (Map<String, Any>) -> Unit
    ) = core.initialize(parameters, callback)

    override fun release() = core.release()

    override fun set(
        api: String,
        parameters: Map<String, Any>,
        callback: (Map<String, Any?>) -> Unit
    ): String = core.set(api, parameters, callback)
}
