package com.mobiledrivetech.external.middleware.util

import com.mobiledrivetech.external.middleware.MiddleWareError
import com.mobiledrivetech.external.middleware.SubError
import com.mobiledrivetech.external.middleware.util.ErrorCode.Companion.paramsNotSet

open class ErrorCode {

    companion object {
        private const val ErrorStart = 2000
        const val unknownError = ErrorStart + 1
        const val facadeNotInitialized = ErrorStart + 2
        const val paramsNotSet = ErrorStart + 3
        const val missingParams = ErrorStart + 4
        const val invalidParams = ErrorStart + 5
        const val componentNotConfigured = ErrorStart + 6
    }
}

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
