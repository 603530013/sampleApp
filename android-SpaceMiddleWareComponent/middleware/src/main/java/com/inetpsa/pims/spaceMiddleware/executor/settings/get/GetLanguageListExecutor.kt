package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetLanguageListExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseLocalExecutor<Unit, Map<String, List<String>>>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): Unit = Unit

    override suspend fun execute(input: Unit): NetworkResponse<Map<String, List<String>>> =
        GetLanguagesLocalesListExecutor(middlewareComponent, params)
            .execute()
            .map { items -> mapOf(Constants.PARAMS_KEY_LANGUAGES to items.map { it.toLanguageTag() }) }
}
