package com.inetpsa.pims.spaceMiddleware.util

import android.util.Base64

@Suppress("SwallowedException", "TooGenericExceptionCaught")
fun ByteArray.getBase64(): String? {
    if (this.isEmpty()) return null
    return try {
        Base64.encodeToString(this, Base64.NO_WRAP)
    } catch (e: Exception) {
        null
    }
}
