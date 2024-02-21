package com.inetpsa.pims.spaceMiddleware

interface IMiddleware {

    fun get(api: String, parameters: Map<String, Any>?, callback: (Map<String, Any?>) -> Unit): String

    fun initialize(parameters: Map<String, Any>, callback: (Map<String, Any>) -> Unit)

    fun release()

    fun set(api: String, parameters: Map<String, Any>, callback: (Map<String, Any?>) -> Unit): String
}
