package com.mobiledrivetech.external.middleware.model

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