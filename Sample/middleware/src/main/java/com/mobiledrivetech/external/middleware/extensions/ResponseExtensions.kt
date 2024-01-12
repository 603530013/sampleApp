package com.mobiledrivetech.external.middleware.extensions

import com.mobiledrivetech.external.middleware.MiddleWareError
import com.mobiledrivetech.external.middleware.model.Response
import com.mobiledrivetech.external.middleware.util.MiddleWareFoundationError

internal inline fun <T> Response<T>.handleResult(
    actionSuccess: (T) -> Unit,
    actionFailure: (MiddleWareError?) -> Unit
) = when (this) {
    is Response.Failure -> actionFailure(this.error)
    is Response.Success -> actionSuccess(this.response)
}

@Throws(MiddleWareError::class)
internal fun <T> Response<T>.unwrap(): T = when (this) {
    is Response.Success -> this.response
    is Response.Failure -> throw this.error ?: MiddleWareFoundationError.unknownError
}

internal fun <T> Response<T>.unwrapNullable(): T? = when (this) {
    is Response.Success -> this.response
    is Response.Failure -> null
}

@Suppress("UNCHECKED_CAST")
internal inline fun <T, R> Response<T>.map(transform: (T) -> R): Response<R> =
    when (this) {
        is Response.Success -> Response.Success(transform(this.response))
        else -> this as Response<R>
    }

internal inline fun <T> Response<T>.ifSuccess(transform: (T) -> Unit): Response<T> {
    if (this is Response.Success) {
        transform(this.response)
    }

    return this
}

@Suppress("UNCHECKED_CAST")
internal inline fun <T, R> Response<T>.map(
    transformSuccess: (T) -> R,
    transformFailure: (MiddleWareError?) -> Response<R>?
): Response<R> = when (this) {
    is Response.Success -> Response.Success(transformSuccess(this.response))
    is Response.Failure -> transformFailure(this.error) ?: this
}

@Suppress("UNCHECKED_CAST")
internal inline fun <T, R> Response<T>.transform(transform: (T) -> Response<R>): Response<R> =
    when (this) {
        is Response.Success -> transform(this.response)
        else -> this as Response<R>
    }

