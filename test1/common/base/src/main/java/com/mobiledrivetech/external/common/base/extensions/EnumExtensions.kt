package com.mobiledrivetech.external.common.base.extensions

inline fun <reified T : Enum<T>> safeValueOf(name: String): T? = try {
    enumValueOf<T>(name)
} catch (ex: IllegalArgumentException) {
    null
}
