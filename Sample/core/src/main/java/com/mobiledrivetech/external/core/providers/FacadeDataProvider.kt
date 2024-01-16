package com.mobiledrivetech.external.core.providers

import com.mobiledrivetech.external.core.data.model.ApiName

interface FacadeDataProvider {
    suspend fun fetch(
        api: ApiName,
        method: Method,
        parameter: Map<String, Any>?
    ): Map<String, Any?>

    enum class Method { INITIALIZE, GET, SET, RELEASE }
}
