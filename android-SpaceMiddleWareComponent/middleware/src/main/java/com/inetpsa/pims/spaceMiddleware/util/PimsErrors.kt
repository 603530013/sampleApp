package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.monitoring.ErrorCode
import com.inetpsa.mmx.foundation.monitoring.ErrorMessage
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSErrorFactory
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError

internal object PimsErrors {

    private const val ERROR_CODE = 2000
    private const val ERROR_CODE_UPDATE_PROFILE_FAILED = ERROR_CODE + 1
    private const val NEED_PIN_TOKEN = ERROR_CODE + 2
    private const val CODE_LOCATION_SERVER_EMPTY_RESPONSE = ERROR_CODE + 3
    private const val CODE_LOCATION_SERVER_ERROR_RESPONSE = ERROR_CODE + 4
    private const val CODE_INVALID_SERVER_URL = ERROR_CODE + 5
    private const val ERROR_CODE_SERVER_FAILED = ERROR_CODE + 6
    private const val ERROR_CODE_TYPE_ERROR = ERROR_CODE + 7
    private const val ERROR_CODE_ALREADY_EXIST = ERROR_CODE + 8

    private const val MESSAGE_LOCATION_SERVER_EMPTY_RESPONSE = "sorry no response found"
    private const val MESSAGE_LOCATION_SERVER_ERROR_RESPONSE = "sorry server error"

    val strongAuth = PIMSErrorFactory.create(ErrorCode.needStrongAuth, ErrorMessage.needStrongAuth)

    fun needPinToken(): PIMSError = PIMSErrorFactory.create(NEED_PIN_TOKEN, "Pin token expired")

    fun apiNotSupported(): PIMSError = PIMSErrorFactory.create(ErrorCode.apiNotSupported, ErrorMessage.apiNotSupported)

    fun updateProfileFailed(): PIMSError =
        PIMSErrorFactory.create(ERROR_CODE_UPDATE_PROFILE_FAILED, "update profile failed")

    fun serverError(errors: List<Any>?, fallback: String) =
        PIMSFoundationError.serverError(
            ERROR_CODE_SERVER_FAILED,
            errors?.toString() ?: fallback
        )

    internal fun zeroResults(message: String? = null): PIMSError = PIMSErrorFactory.create(
        CODE_LOCATION_SERVER_EMPTY_RESPONSE,
        message ?: MESSAGE_LOCATION_SERVER_EMPTY_RESPONSE
    )

    internal fun locationServerError(message: String? = null): PIMSError = PIMSFoundationError.serverError(
        CODE_LOCATION_SERVER_ERROR_RESPONSE,
        message ?: MESSAGE_LOCATION_SERVER_ERROR_RESPONSE
    )

    internal fun invalidServerUrl(url: String): PIMSError = PIMSErrorFactory.create(
        CODE_INVALID_SERVER_URL,
        "invalid server url: $url"
    )

    internal fun typeError(message: String): PIMSError = PIMSErrorFactory.create(ERROR_CODE_TYPE_ERROR, message)

    internal fun alreadyExist(name: String): PIMSError =
        PIMSErrorFactory.create(ERROR_CODE_ALREADY_EXIST, "$name already exist")
}
