package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.SettingsCallCenterItemResponse

internal class GetSettingsFCAExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<String, List<SettingsCallCenterItemResponse>>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<List<SettingsCallCenterItemResponse>> {
        val request = request(
            type = object : TypeToken<List<SettingsCallCenterItemResponse>>() {}.type,
            urls = arrayOf("/v3/accounts/", uid, "/vehicles/", input, "/settings"),
            queries = mapOf("settingType" to "PhoneNumbers")
        )

        return communicationManager.get(request, TokenType.AWSToken(FCAApiKey.SDP))
    }
}
