package com.inetpsa.pims.spaceMiddleware.util

import java.util.Locale

/**
 * Convert a string based locale into a Locale Object.
 * Assumes the string has form "{language}_{country}_{variant}".
 * Examples: "en", "de_DE", "_GB", "en_US_WIN", "de__POSIX", "fr_MAC"
 *
 * @return the Locale
 */
internal fun String?.toLocale(): Locale? =
    this?.trim()
        ?.takeIf { it.isNotBlank() }
        ?.let { localeString ->
            when (localeString.equals("default", true)) {
                true -> Locale.getDefault()

                else -> {
                    val parts = localeString.split("_", limit = 3)
                    when (parts.size) {
                        0 -> null
                        1 -> Locale(parts.first())
                        2 -> Locale(parts.first(), parts[1])
                        else -> Locale(parts.first(), parts[1], parts[2])
                    }
                }
            }
        }
