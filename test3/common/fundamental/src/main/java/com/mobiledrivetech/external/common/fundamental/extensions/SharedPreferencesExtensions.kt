package com.mobiledrivetech.external.common.fundamental.extensions

import android.content.SharedPreferences

inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T = when (T::class) {
    Boolean::class -> getBoolean(key, defaultValue as Boolean) as T
    Int::class -> getInt(key, defaultValue as Int) as T
    Long::class -> getLong(key, defaultValue as Long) as T
    Number::class -> getFloat(key, defaultValue as Float) as T
    String::class -> getString(key, defaultValue as String) as T
    Set::class -> getStringSet(key, defaultValue as Set<String>) as T
    else -> throw IllegalArgumentException("${T::class.java.simpleName} is not supported")
}

inline fun <reified T> SharedPreferences.put(key: String, value: T) = with(edit()) {
    when (T::class) {
        Boolean::class -> putBoolean(key, value as Boolean)
        Int::class -> putInt(key, value as Int)
        Long::class -> putLong(key, value as Long)
        Number::class -> putFloat(key, value as Float)
        String::class -> putString(key, value as String)
        Set::class -> putStringSet(key, value as Set<String>)
        else -> throw IllegalArgumentException("${T::class.java.simpleName} is not supported")
    }
    apply()
}

inline fun <reified T : Enum<T>> SharedPreferences.getEnum(
    key: String,
    defaultEnum: T
): T {
    val input = getString(key, null)
    return when (input.isNullOrBlank()) {
        true -> defaultEnum
        else -> enumValueOf(input)
    }
}

inline fun <reified T : Enum<out T>> SharedPreferences.putEnum(
    key: String,
    enumValue: T
) {
    with(edit()) {
        putString(key, enumValue.name)
        apply()
    }
}
