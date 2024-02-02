package com.mobiledrivetech.external.middleware.util

import com.mobiledrivetech.external.middleware.model.ErrorCode
import com.mobiledrivetech.external.middleware.model.ErrorCode.Companion.paramsNotSet
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.model.SubError

open class ErrorMessage {

    companion object {
        const val unknownError = "Unknown error"
        const val facadeNotInitialized = "Facade not initialized"
        fun missingParams(param: String) = "Missing $param parameter"
        fun invalidParams(param: String) = "Invalid $param parameter"
        fun paramsNotSet(param: String) = "$param parameter not set"
        fun componentNotConfigured(param: String) = "$param not configured"
    }
}

object MiddleWareErrorFactory {

    fun create(code: Int, message: String, subError: SubError? = null) =
        MiddleWareError(code = code, message = message, subError = subError)

    fun create(code: Int, message: String, subError: SubError? = null, info: Map<String, Any>?) =
        MiddleWareError(code = code, message = message, subError = subError, info = info)
}

object MiddleWareFoundationError {

    fun missingParameter(name: String) = MiddleWareErrorFactory.create(
        ErrorCode.missingParams, ErrorMessage.missingParams(name)
    )

    fun invalidParameter(name: String) = MiddleWareErrorFactory.create(
        ErrorCode.invalidParams, ErrorMessage.invalidParams(name)
    )

    val unknownError =
        MiddleWareErrorFactory.create(ErrorCode.unknownError, ErrorMessage.unknownError)

    val facadeNotInit = MiddleWareError(
        ErrorCode.facadeNotInitialized,
        ErrorMessage.facadeNotInitialized
    )

    fun paramNotSet(name: String) =
        MiddleWareErrorFactory.create(paramsNotSet, ErrorMessage.paramsNotSet(name))

    fun unknownError(reason: String) = MiddleWareErrorFactory.create(
        ErrorCode.unknownError,
        reason
    )

    fun componentNotConfigured(name: String) = MiddleWareErrorFactory.create(
        ErrorCode.componentNotConfigured, ErrorMessage.componentNotConfigured(name)
    )
}
