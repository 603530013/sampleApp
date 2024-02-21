package com.inetpsa.pims.spaceMiddleware.util

import android.text.Html
import android.util.Patterns
import androidx.annotation.VisibleForTesting

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun String?.normaliseFromHtml(): String? =
    this?.takeIf { it.isNotBlank() }
        ?.let { Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY) }
        ?.toString()
        ?.trim()
fun String?.isValidWebUrl(): Boolean = this.takeIf { !it.isNullOrBlank() }
    ?.let { Patterns.WEB_URL.matcher(it).matches() } ?: false

@Throws(IllegalArgumentException::class)
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal infix fun String?.compareVersion(remote: String?): Int {
    // Split the version strings into individual components and map them to integers
    val v1 = this?.split(".")?.map { it.toIntOrNull() ?: 0 } ?: emptyList()
    val v2 = remote?.split(".")?.map { it.toIntOrNull() ?: 0 } ?: emptyList()

    // Determine the maximum length between the two version lists
    val maxLength = maxOf(v1.size, v2.size)

    // Compare each component of the versions
    for (i in 0 until maxLength) {
        // Retrieve the components at the current index or default to 0
        val num1 = v1.getOrNull(i) ?: 0
        val num2 = v2.getOrNull(i) ?: 0

        // Compare the components
        if (num1 != num2) {
            // Return the comparison result if they are not equal
            return num1.compareTo(num2)
        }
    }

    // All components are equal, return 0
    return 0
}
