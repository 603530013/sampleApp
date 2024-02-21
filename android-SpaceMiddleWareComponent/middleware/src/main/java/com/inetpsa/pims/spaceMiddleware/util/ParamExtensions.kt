@file:Suppress("TooManyFunctions")

package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.ErrorCode
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.tools.Environment
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

@Throws(PIMSError::class)
internal infix fun Map<String, Any?>?.hasEnvironment(name: String): Environment =
    hasEnvironmentOrNull(name) ?: throw PIMSFoundationError.invalidParameter(name)

@Throws(PIMSError::class)
internal infix fun Map<String, Any?>?.hasEnvironmentOrNull(name: String): Environment? {
    return when (this.hasOrNull<String>(name)) {
        "Development" -> Environment.DEV
        "PreProduction" -> Environment.PREPROD
        "Production" -> Environment.PROD
        else -> null
    }
}

@Suppress("SwallowedException")
internal inline infix fun <reified T> Map<String, Any?>?.hasOrNull(name: String): T? = try {
    this has name
} catch (e: PIMSError) {
    if (e.code == ErrorCode.missingParams) {
        null
    } else {
        throw e
    }
}

@Suppress("SwallowedException")
internal inline fun <reified T> Map<String, Any?>?.has(name: String, optional: Boolean): T? = when (optional) {
    true -> try {
        this has name
    } catch (e: PIMSError) {
        null
    }

    else -> this has name
}

@Throws(PIMSError::class)
internal inline infix fun <reified T : Enum<T>> Map<String, Any?>?.hasEnum(name: String): T {
    val value: String = this has name
    return value.toEnumOrDefault<T>() ?: throw PIMSFoundationError.invalidParameter(name)
}

internal inline infix fun <reified T : Enum<in T>> Map<String, Any?>?.hasEnumNullable(name: String): T? {
    val value: String? = this hasOrNull name
    return value?.toEnumOrDefault<T>()
}

internal inline fun <reified T : Enum<T>> Map<String, Any?>?.hasEnum(name: String, fallback: T): T {
    val value: String? = this hasOrNull name
    return value?.toEnumOrDefault<T>() ?: fallback
}

/**
 * Help to transform string to [Enum] object
 * @receiver String? : the input value
 * @return T? : the Enum result value
 */
@Suppress("SwallowedException")
internal inline fun <reified T : Enum<T>> String?.toEnumOrDefault(): T? {
    if (this == null) {
        return null
    }

    return try {
        enumValues<T>().firstOrNull { it.name.equals(this, true) }
    } catch (e: IllegalArgumentException) {
        null
    }
}

/**
 * Returns a map containing all key-value pairs with values matching the given predicate.
 *
 * The returned map preserves the entry iteration order of the original map.
 */
@Suppress("UNCHECKED_CAST")
internal fun <K, V> Map<out K, V?>.filterNotNull(): Map<K, V> =
    filterKeys { key -> ((key as? String)?.isNotBlank() ?: key) != null }
        .filterValues { value -> (value as? String)?.isNotBlank() ?: (value != null) } as Map<K, V>

internal fun Map<String, String?>.formatToQuery(): String {
    val query = StringBuilder("")
    entries.forEachIndexed { index, entry ->
        when (index) {
            0 -> query.append("?")
            else -> query.append("&")
        }
        query.append("${entry.key}=${entry.value}")
    }
    return query.toString()
}

/**
 * Try to extract value from map using key and be sure to be a localDate type using the iso (2011-12-03)
 * @param name
 * @return
 */
@Throws(PIMSError::class)
internal infix fun Map<String, Any?>?.hasLocalDate(name: String): LocalDate {
    val value: String = this has name
    try {
        return LocalDate.parse(value)
    } catch (ex: DateTimeParseException) {
        throw PIMSFoundationError.invalidParameter(name)
    }
}

@Throws(PIMSError::class)
internal infix fun Map<String, Any?>?.hasLocalDateTime(name: String): LocalDateTime {
    val value: String = this has name
    return try {
        LocalDateTime.parse(value)
    } catch (ex: DateTimeParseException) {
        throw PIMSFoundationError.invalidParameter(name)
    }
}
