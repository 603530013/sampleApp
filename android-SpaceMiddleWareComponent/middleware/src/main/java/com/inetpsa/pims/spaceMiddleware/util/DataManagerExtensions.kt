package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal suspend fun MiddlewareComponent.createSync(key: String, data: Any, mode: StoreMode): Boolean {
    val generatedKey = generateStorageKey(key)
    return suspendCancellableCoroutine { cont ->
        dataManager.create(generatedKey, data, mode) { result ->
            cont.resume(result)
        }
    }
}

internal suspend fun MiddlewareComponent.deleteSync(key: String, mode: StoreMode): Boolean {
    val generatedKey = generateStorageKey(key)
    return suspendCancellableCoroutine { cont ->
        dataManager.delete(generatedKey, mode) { result ->
            cont.resume(result)
        }
    }
}

internal suspend fun <T : Any> MiddlewareComponent.updateSync(key: String, data: T, mode: StoreMode): Boolean {
    val generatedKey = generateStorageKey(key)
    return suspendCancellableCoroutine { cont ->
        dataManager.update(key = generatedKey, data = data, mode = mode) { result ->
            cont.resume(result)
        }
    }
}

internal inline fun <reified T : Any?> MiddlewareComponent.readSync(key: String, mode: StoreMode): T? {
    val generatedKey = generateStorageKey(key)
    val data = dataManager.read(key = generatedKey, mode = mode)
    return when (T::class) {
        Boolean::class, Float::class, Int::class, Long::class, String::class -> data as? T
        else -> (data as? String)
            .takeIf { !it.isNullOrBlank() }
            ?.fromJson<T>()
    }
}
