package com.inetpsa.pims.spaceMiddleware.executor.settings

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.settings.Settings
import com.inetpsa.pims.spaceMiddleware.model.settings.Settings.CallCenterSettingsFca
import com.inetpsa.pims.spaceMiddleware.util.map

@Deprecated("This should be replaced by GetSettingsFCAExecutor")
internal class GetCallCenterSettingsFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<String, Settings>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<Settings> {
        val request = request(
            type = object : TypeToken<List<CallCenterSettingsFca>>() {}.type,
            urls = arrayOf("/v3/accounts/", uid, "/vehicles/", input, "/settings"),
            queries = mapOf("settingType" to "PhoneNumbers")
        )

        val response: NetworkResponse<List<CallCenterSettingsFca>> = communicationManager.get(
            request,
            TokenType.AWSToken(FCAApiKey.SDP)
        )
        return response.map {
            Settings(callCenterSettings = it)
        }
    }
}
