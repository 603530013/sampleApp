package com.mobiledrivetech.external.middleware.extensions

import android.util.Log
import com.mobiledrivetech.external.middleware.MiddleWareError
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog

/**
 * Transform [MiddleWareError] to [Map]
 * @receiver CarKeyError? : the input object
 * @return Map<String, *>: if input is null we return empty map
 */
fun MiddleWareError?.asMap(): Map<String, Any> {
    Log.v("", "$this")

    val result = this?.let { error ->
        hashMapOf<String, Any>().apply {
            this["code"] = error.code
            this["label"] = error.message ?: ""
            subError?.let {
                this["subCode"] = hashMapOf<String, Any>()
                    .apply {
                        this@apply["code"] = subError.status
                        this@apply["label"] = subError.body
                    }
            }

            info?.let {
                this["info"] = it
            }
        }
    } ?: mapOf()

    MDLog.debug("<-- result: $result")
    return result
}