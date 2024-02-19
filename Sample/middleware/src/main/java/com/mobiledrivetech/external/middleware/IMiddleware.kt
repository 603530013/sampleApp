package com.mobiledrivetech.external.middleware

/**
 * Middleware interface for communication with middleware by other modules
 */
interface IMiddleware {

    /**
     * Get method for fetching data from middleware
     *
     * @param api with [String] which will be used to define which api will be used
     * @param parameters with parameters which will be used to set parameters the [api]
     * @param callback with response from middleware
     * @return with response from middleware
     */
    fun get(
        api: String,
        parameters: Map<String, Any>?,
        callback: (Map<String, Any?>) -> Unit
    ): String

    /**
     * Initialize method for initializing middleware
     *
     * @param parameters with parameters for initialization
     * @param callback with callback for initialization
     */
    fun initialize(parameters: Map<String, Any>, callback: (Map<String, Any>) -> Unit)

    /**
     * Release method for releasing middleware
     */
    fun release()

    /**
     * Set method for setting data to middleware
     *
     * @param api with [String] which will be used to define which api will be used
     * @param parameters with parameters which will be used to set parameters the [api]
     * @param callback with response from middleware
     * @return with response from middleware
     */
    fun set(
        api: String,
        parameters: Map<String, Any>,
        callback: (Map<String, Any?>) -> Unit
    ): String
}
