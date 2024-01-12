package com.mobiledrivetech.external.middleware

import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog

const val STATUS = "status"
const val BODY = "body"

data class SubError(val status: Int, val body: String) {

    fun toMap(): Map<String, Any> {
        MDLog.debug("-->")
        val result = mapOf(
            Pair(STATUS, status),
            Pair(BODY, body)
        )
        MDLog.debug("<-- result: $result")
        return result
    }
}
