package com.mobiledrivetech.external.common.base.extensions

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.util.PatternsCompat

fun Bundle.getNullableInt(key: String, default: Int? = null): Int? =
    getInt(key, default ?: -1).let { if (it == -1) null else it }

fun Bundle?.getLocationExtras(): String? = this?.getString(Intent.EXTRA_TEXT)?.let { input ->
    input.extractFirstLink()?.let { link ->
        val host = link.host
        val hasLocationHost = !host.isNullOrBlank() &&
            (host.contains("maps", true) || host.contains("waze", true))
        when (hasLocationHost) {
            true -> input
            else -> null
        }
    }
}

fun CharSequence.extractFirstLink() = split(" ")
    .firstOrNull { PatternsCompat.WEB_URL.matcher(it).find() }
    ?.toUri()
