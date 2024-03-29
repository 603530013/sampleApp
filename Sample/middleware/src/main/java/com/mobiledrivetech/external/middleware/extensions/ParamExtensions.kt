package com.mobiledrivetech.external.middleware.extensions

import com.mobiledrivetech.external.middleware.foundation.models.Environment
import com.mobiledrivetech.external.middleware.model.ErrorCode
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.util.MiddleWareFoundationError

@Throws(MiddleWareError::class)
internal infix fun Map<String, Any?>?.hasEnvironment(name: String): Environment =
    hasEnvironmentOrNull(name) ?: throw MiddleWareFoundationError.invalidParameter(name)

@Throws(MiddleWareError::class)
internal infix fun Map<String, Any?>?.hasEnvironmentOrNull(name: String): Environment? =
    when (this.hasOrNull<String>(name)) {
        "Development" -> Environment.DEV
        else -> null
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
    if (optional) {
        try {
            this has name
        } catch (e: MiddleWareError) {
            throw e
        }
    } else {
        this has name
    }

@Throws(MiddleWareError::class)
internal inline infix fun <reified T> Map<String, Any?>?.hasEnum(name: String): T where T : EnumValue, T : Enum<T> {
    val value: String = this has name
    return value.toEnumOrDefault() ?: throw MiddleWareFoundationError.invalidParameter(name)
}

internal inline infix fun <reified T> Map<String, Any?>?.hasEnumNullable(name: String): T? where T : EnumValue, T : Enum<T> {
    val value: String? = this hasOrNull name
    return value?.toEnumOrDefault()
}

internal inline fun <reified T> Map<String, Any?>?.hasEnum(
    name: String,
    fallback: T
): T where T : EnumValue, T : Enum<T> {
    val value: String? = this hasOrNull name
    return value?.toEnumOrDefault() ?: fallback
}
