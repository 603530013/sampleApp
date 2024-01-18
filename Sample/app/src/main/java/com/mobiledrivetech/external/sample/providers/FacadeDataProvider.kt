package com.mobiledrivetech.external.sample.providers

import com.mobiledrivetech.external.sample.data.model.ApiName

interface FacadeDataProvider {
    suspend fun fetch(
        api: ApiName,
        method: Method,
        parameter: Map<String, Any>?
    ): Map<String, Any?>

    enum class Method { INITIALIZE, GET, SET, RELEASE }
}
