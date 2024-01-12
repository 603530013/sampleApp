package com.mobiledrivetech.external.middleware.extensions

inline fun <reified T : Enum<T>> Array<T>.valueOf(value: String, ignoreCase: Boolean = true) =
    enumValues<T>().asSequence().firstOrNull { it.name.equals(value, ignoreCase) }

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
inline fun <reified T> String?.toEnumOrDefault(): T? where T : EnumValue, T : Enum<T> {
    this.let {
        return try {
            enumValues<T>().firstOrNull { it.value.equals(this, true) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
