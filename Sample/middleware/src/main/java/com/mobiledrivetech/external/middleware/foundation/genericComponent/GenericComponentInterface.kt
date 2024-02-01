package com.mobiledrivetech.external.middleware.foundation.genericComponent

interface GenericComponentInterface {

    /**
     * Initialize component
     * @param parameters: map<String, Any> contains the parameters
     * @param callback: to be invoked on response
     */
    fun initialize(
        parameters: Map<String, Any>,
        callback: (Map<String, Any>) -> Unit
    )

    /**
     * Release the component
     */
    fun release()

    /**
     * Execute GET command
     * @param api command name
     * @param parameters parameters to use to execute the command
     * @param callback callback to be invoked on response
     */
    fun get(
        api: String,
        parameters: Map<String, Any>? = null,
        callback: (Map<String, Any?>) -> Unit
    ): String

    /**
     * Execute SET command
     * @param api command name
     * @param parameters parameters to use to execute the command
     * @param callback callback to be invoked on response
     */
    fun set(
        api: String,
        parameters: Map<String, Any>,
        callback: (Map<String, Any?>) -> Unit
    ): String

    /**
     * Execute SUBSCRIBE command
     * @param api command name
     * @param callback callback to be invoked on response
     */
    fun subscribe(
        api: String,
        parameters: Map<String, Any>? = null,
        callback: (Map<String, Any?>) -> Unit
    ): String

    /**
     * Execute UNSUBSCRIBE command
     * @param api command name
     * @param callback callback to be invoked on response
     */
    fun unsubscribe(api: String, callback: (Map<String, Any?>) -> Unit): String
}
