package com.inetpsa.pims.spaceMiddleware.util

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.ErrorCode
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.R
import java.net.HttpURLConnection

internal inline fun <T> NetworkResponse<T>.handleResult(
    actionSuccess: (T) -> Unit,
    actionFailure: (PIMSError?) -> Unit
) {
    when (this) {
        is NetworkResponse.Failure -> actionFailure(this.error)

        is NetworkResponse.Success -> actionSuccess(this.response)
    }
}

@Throws(PIMSError::class)
internal fun <T> NetworkResponse<T>.unwrap(): T = when (this) {
    is NetworkResponse.Success -> this.response
    is NetworkResponse.Failure -> throw this.error ?: PIMSFoundationError.unknownError
}

internal fun <T> NetworkResponse<T>.unwrapNullable(): T? = when (this) {
    is NetworkResponse.Success -> this.response
    is NetworkResponse.Failure -> null
}

@Suppress("UNCHECKED_CAST")
internal inline fun <T, R> NetworkResponse<T>.map(transform: (T) -> R): NetworkResponse<R> = when (this) {
    is NetworkResponse.Success -> NetworkResponse.Success(transform(this.response))
    else -> this as NetworkResponse<R>
}

internal inline fun <T> NetworkResponse<T>.ifSuccess(transform: (T) -> Unit): NetworkResponse<T> {
    if (this is NetworkResponse.Success) {
        transform(this.response)
    }

    return this
}

@Suppress("UNCHECKED_CAST")
internal inline fun <T, R> NetworkResponse<T>.map(
    transformSuccess: (T) -> R,
    transformFailure: (PIMSError?) -> NetworkResponse<R>?
): NetworkResponse<R> = when (this) {
    is NetworkResponse.Success -> NetworkResponse.Success(transformSuccess(this.response))
    is NetworkResponse.Failure -> transformFailure(this.error) ?: this
    else -> transformFailure(null) ?: (this as NetworkResponse<R>)
}

@Suppress("UNCHECKED_CAST")
internal inline fun <T, R> NetworkResponse<T>.transform(transform: (T) -> NetworkResponse<R>): NetworkResponse<R> =
    when (this) {
        is NetworkResponse.Success -> transform(this.response)
        else -> this as NetworkResponse<R>
    }

@Suppress("UNCHECKED_CAST")
internal inline fun <T> NetworkResponse<T>.mapStrongAuthFailure(): NetworkResponse<T> =
    when (this) {
        is NetworkResponse.Success -> this

        is NetworkResponse.Failure -> handleStrongAuth()
    }

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun NetworkResponse.Failure.handleStrongAuth(): NetworkResponse.Failure =
    when (error.isPinTokenForbidden()) {
        true -> NetworkResponse.Failure(PimsErrors.strongAuth)
        else -> this
    }

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun PIMSError?.isPinTokenForbidden(): Boolean {
    return this?.code == ErrorCode.serverError &&
        subError?.status == HttpURLConnection.HTTP_FORBIDDEN &&
        subError?.body.equals("The token is expired", ignoreCase = true)
}
