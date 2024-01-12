package com.mobiledrivetech.external.middleware

class MiddleWareError(val code: Int, override val message: String, val subError: SubError? = null) :
    Error(message, null, true, false) {

    var info: Map<String, Any>? = null

    constructor(
        code: Int,
        message: String, subError: SubError? = null, info: Map<String, Any>? = null
    ) : this(code, message, subError) {
        this.info = info
    }

    override fun toString(): String =
        subError?.let {
            val result =
                "MiddleWareError($code, $message), subError(${subError.status}, ${subError.body})"
            info?.let {
                "$result, info:${info}"
            } ?: kotlin.run {
                result
            }
        } ?: kotlin.run {
            val result = "MiddleWareError($code, $message)"

            info?.let {
                "$result, info:${info}"
            } ?: kotlin.run {
                result
            }
        }
}
