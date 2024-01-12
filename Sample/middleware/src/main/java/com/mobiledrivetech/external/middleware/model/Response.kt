package com.mobiledrivetech.external.middleware.model

import com.mobiledrivetech.external.middleware.MiddleWareError

sealed class Response<out T> {
    data class Success<out T>(val response: T) : Response<T>()
    data class Failure(val error: MiddleWareError?) : Response<Nothing>()
}
