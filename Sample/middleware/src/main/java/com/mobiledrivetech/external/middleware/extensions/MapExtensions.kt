package com.mobiledrivetech.external.middleware.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mobiledrivetech.external.middleware.MiddleWareError
import com.mobiledrivetech.external.middleware.util.ErrorCode
import com.mobiledrivetech.external.middleware.util.ErrorMessage
import com.mobiledrivetech.external.middleware.util.MiddleWareErrorFactory

/**
 * Recursive sort of map and it children
 */
fun Map<String, Any?>.sort(): Map<String, Any?> {
    val sortedMap = this.toSortedMap(compareBy { it })
    for ((k, v) in sortedMap) {
        when (v) {
            is Map<*, *> -> {
                sortedMap[k] = (v as Map<String, Any>).sort()
            }

            is List<*> -> {
                if (v.isNotEmpty() && v[0] is Map<*, *>) {
                    val list = mutableListOf<Any>()
                    v.forEach { it ->
                        val mapSort = (it as Map<String, Any>).sort()
                        list.add(mapSort)
                        list.sortBy { it.toString() }
                    }
                    sortedMap[k] = list
                } else {
                    sortedMap[k] = v.sortedBy { it.toString() }
                }
            }
        }
    }
    return sortedMap
}

/**
 * check if map input is valid or not
 * @receiver Map<String, Any?>?
 * @param name String
 * @return T
 */
@Throws(MiddleWareError::class)
inline infix fun <reified T> Map<String, Any?>?.has(name: String): T =
    when {
        this == null -> throw name.toMissingParamError()
        this.isEmpty() -> throw name.toMissingParamError()
        !this.containsKey(name) -> throw name.toMissingParamError()
        this[name] !is T -> throw name.toInvalidParamError()
        (this[name] is String) && (this[name] as String).isBlank() ->
            throw name.toMissingParamError()

        this[name] is T -> this[name] as T
        else -> throw name.toMissingParamError()
    }


fun String.toMissingParamError() = MiddleWareErrorFactory.create(
    ErrorCode.missingParams, ErrorMessage.missingParams(this)
)

fun String.toInvalidParamError() = MiddleWareErrorFactory.create(
    ErrorCode.invalidParams, ErrorMessage.invalidParams(this)
)

val gson: Gson = GsonBuilder()
    .enableComplexMapKeySerialization()
    .disableHtmlEscaping()
    .serializeNulls()
    .create()
