package com.mobiledrivetech.external.core.data.model

import androidx.annotation.Keep

sealed class ApiName(val name: String) {
    @Keep
    sealed class Middleware(name: String) : ApiName(name) {
        object Initialize : Middleware("mobiledrivetech.middleware.initialize")
        object Test : Middleware("mobiledrivetech.middleware.test")
    }
}