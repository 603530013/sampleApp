package com.mobiledrivetech.external.middleware

import android.util.Log

const val STATUS = "status"
const val BODY = "body"

data class SubError(val status: Int, val body: String) {

    fun toMap(): Map<String, Any> {
        Log.v("", "-->")
        val result = mapOf(
            Pair(STATUS, status),
            Pair(BODY, body)
        )
        Log.v("", "<-- result: $result")
        return result
    }
}
