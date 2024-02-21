package com.mobiledrivetech.external.middleware.extensions

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.model.MiddleWareError

/**
 * Transform [MiddleWareError] to [Map]
 * @receiver CarKeyError? : the input object
 * @return Map<String, *>: if input is null we return empty map
 */
fun MiddleWareError?.asMap(): Map<String, Any> {
    MDLog.debug("$this")

    val result = this?.let { error ->
        hashMapOf<String, Any>().apply {
            this[Constants.PARAMS_KEY_CODE] = error.code
            this[Constants.PARAMS_KEY_LABEL] = error.message ?: ""
            subError?.let {
                this[Constants.PARAMS_KEY_SUB_CODE] = hashMapOf<String, Any>()
                    .apply {
                        this@apply[Constants.PARAMS_KEY_CODE] = subError.status
                        this@apply[Constants.PARAMS_KEY_LABEL] = subError.body
                    }
            }

            info?.let {
                this[Constants.PARAMS_KEY_INFO] = it
            }
        }
    } ?: mapOf()

    MDLog.debug("<-- result: $result")
    return result
}