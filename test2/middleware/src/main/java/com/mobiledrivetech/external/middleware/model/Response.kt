package com.mobiledrivetech.external.middleware.model

sealed class Response<out T> {
    data class Success<out T>(val response: T) : Response<T>()
    data class Failure(val error: MiddleWareError?) : Response<Nothing>()
}
