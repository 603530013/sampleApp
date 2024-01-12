package com.mobiledrivetech.external.middleware.util

import com.mobiledrivetech.external.middleware.MiddleWareError
import com.mobiledrivetech.external.middleware.extensions.EnumValue
import com.mobiledrivetech.external.middleware.extensions.has
import com.mobiledrivetech.external.middleware.extensions.toEnumOrDefault
import com.mobiledrivetech.external.middleware.foundation.models.Environment

@Throws(MiddleWareError::class)
internal infix fun Map<String, Any?>?.hasEnvironment(name: String): Environment =
    hasEnvironmentOrNull(name) ?: throw MiddleWareFoundationError.invalidParameter(name)

@Throws(MiddleWareError::class)
internal infix fun Map<String, Any?>?.hasEnvironmentOrNull(name: String): Environment? {
    return when (this.hasOrNull<String>(name)) {
        "Development" -> Environment.DEV
        else -> null
    }
}

@Suppress("SwallowedException")
internal inline infix fun <reified T> Map<String, Any?>?.hasOrNull(name: String): T? = try {
    this has name
} catch (e: MiddleWareError) {
    if (e.code == ErrorCode.missingParams) {
        null
    } else {
        throw e
    }
}

@Suppress("SwallowedException")
internal inline fun <reified T> Map<String, Any?>?.has(name: String, optional: Boolean): T? =
    when (optional) {
        true -> try {
            this has name
        } catch (e: MiddleWareError) {
            null
        }

        else -> this has name
    }

@Throws(MiddleWareError::class)
internal inline infix fun <reified T> Map<String, Any?>?.hasEnum(name: String): T where T : EnumValue, T : Enum<T> {
    val value: String = this has name
    return value.toEnumOrDefault<T>() ?: throw MiddleWareFoundationError.invalidParameter(name)
}

internal inline infix fun <reified T> Map<String, Any?>?.hasEnumNullable(name: String): T? where T : EnumValue, T : Enum<T> {
    val value: String? = this hasOrNull name
    return value?.toEnumOrDefault<T>()
}

internal inline fun <reified T> Map<String, Any?>?.hasEnum(
    name: String,
    fallback: T
): T where T : EnumValue, T : Enum<T> {
    val value: String? = this hasOrNull name
    return value?.toEnumOrDefault<T>() ?: fallback
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
