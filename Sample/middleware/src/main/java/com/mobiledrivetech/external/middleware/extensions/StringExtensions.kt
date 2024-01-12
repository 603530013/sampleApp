package com.mobiledrivetech.external.middleware.extensions

import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.mobiledrivetech.external.middleware.util.MiddleWareFoundationError


fun String.hide() =
    this.replaceRange(startIndex = (this.length / 2), endIndex = this.length, replacement = "★".repeat(kotlin.math.abs(this.length - ((this.length / 2)))))

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal inline fun <reified T> T.asJson(): String = try {
    when (this) {
        is String -> this
        else -> gson.toJson(this, T::class.java)
    }
} catch (ex: Exception) {
    throw MiddleWareFoundationError.unknownError
}

/**
 * Transform json [String] to [Map]
 * @receiver String: any nullable string as input
 * @return Map<String, Any?>: return empty map if the input is not json data
 */
internal fun String?.asMap(): Map<String, Any?> = when (isNullOrBlank()) {
    true -> mapOf()

    else -> try {
        val type = object : TypeToken<Map<String, Any?>>() {}.type
        gson.fromJson(this, type)
    } catch (ex: JsonParseException) {
        mapOf()
    }
}