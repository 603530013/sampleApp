package com.mobiledrivetech.external.sample.providers

import com.mobiledrivetech.external.sample.data.model.ApiName

interface FacadeDataProvider {
    /**
     * Fetch data from middleware
     *
     * @param api with [ApiName] which will be used to define which api will be used
     * @param method with [Method] which will be used to define which method will be used
     * @param parameter with parameters which will be used to set parameters the [api]
     * @return with response from middleware
     */
    suspend fun fetch(
        api: ApiName,
        method: Method,
        parameter: Map<String, Any>?
    ): Map<String, Any?>

    /**
     * Method which will be used for fetching data. It can be used for initialization, get, set and release
     */
    enum class Method { INITIALIZE, GET, SET, RELEASE }
}
