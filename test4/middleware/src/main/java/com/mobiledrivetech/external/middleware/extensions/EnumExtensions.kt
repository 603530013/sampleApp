package com.mobiledrivetech.external.middleware.extensions

/**
 * interface to simplify extracting value from enum
 * @property value String
 */
interface EnumValue {

    val value: String
}

/**
 * Help to transform string to [Enum] object
 * @receiver String? : the input value
 * @return T? : the Enum result value
 */
inline fun <reified T> String?.toEnumOrDefault(): T? where T : EnumValue, T : Enum<T> =
    this.let {
        try {
            enumValues<T>().firstOrNull { it.value.equals(this, true) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }
