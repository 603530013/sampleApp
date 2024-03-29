package com.mobiledrivetech.external.sample.providers

import androidx.annotation.VisibleForTesting
import com.mobiledrivetech.external.middleware.IMiddleware
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.sample.data.model.ApiName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FacadeDataProviderImp(private val middleware: IMiddleware) : FacadeDataProvider {
    override suspend fun fetch(
        api: ApiName,
        method: FacadeDataProvider.Method,
        parameter: Map<String, Any>?
    ): Map<String, Any?> {
        MDLog.debug(tag = "middleware test", message = "fetch | ${method.name} | $api | $parameter")
        return when (method) {
            FacadeDataProvider.Method.INITIALIZE -> initialize(parameter)
            FacadeDataProvider.Method.GET -> get(api, parameter)
            FacadeDataProvider.Method.SET -> set(api, parameter)
            FacadeDataProvider.Method.RELEASE -> release()
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun initialize(parameter: Map<String, Any>?): Map<String, Any?> =
        suspendCancellableCoroutine { continuation ->
            middleware.initialize(parameter.orEmpty()) { result ->
                MDLog.inform(
                    tag = "middleware test",
                    message = "initialize | response | parameters $parameter | response: $result"
                )
                continuation.resume(result)
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun get(
        api: ApiName,
        parameter: Map<String, Any>?
    ): Map<String, Any?> =
        suspendCancellableCoroutine { continuation ->
            middleware.get(api.name, parameter ?: emptyMap()) { result ->
                MDLog.inform(
                    tag = "middleware test",
                    message = "get | api: $api | parameters $parameter | response: $result"
                )
                continuation.resume(result)
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun set(
        api: ApiName,
        parameter: Map<String, Any>?
    ): Map<String, Any?> =
        suspendCancellableCoroutine { continuation ->
            middleware.set(api.name, parameter ?: emptyMap()) { result ->
                MDLog.inform(
                    tag = "middleware test",
                    message = "set | api: $api | parameters $parameter | response: $result"
                )
                continuation.resume(result)
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun release() = coroutineScope {
        MDLog.inform(tag = "middleware test", message = "release")
        middleware.release()
        emptyMap<String, Any?>()
    }
}
