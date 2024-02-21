package com.inetpsa.pims.spaceMiddleware.util

import androidx.annotation.NonNull
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.tools.jsonadapters.ZonedDateTimeTypeAdapter
import java.time.ZonedDateTime

object Utils {

    @NonNull
    internal fun getJsonClient() = GsonBuilder()
        .enableComplexMapKeySerialization()
        .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeTypeAdapter())
        .create()
}

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal inline fun <reified T> T.asJson(): String = try {
    when (this) {
        is String -> this
        else -> Utils.getJsonClient().toJson(this, T::class.java)
    }
} catch (ex: Exception) {
    throw PIMSFoundationError.unknownError
}

/**
 * Transfrom a string to adequate model
 *
 * @param T
 * @return
 */
internal inline fun <reified T> String.fromJson(): T? = try {
    Utils.getJsonClient().fromJson(this, T::class.java)
} catch (ex: JsonParseException) {
    null
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
        Utils.getJsonClient().fromJson(this, type)
    } catch (ex: JsonParseException) {
        mapOf()
    }
}
