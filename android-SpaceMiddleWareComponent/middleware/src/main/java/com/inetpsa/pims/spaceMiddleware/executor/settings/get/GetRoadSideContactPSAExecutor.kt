package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.util.map

@Deprecated("this was moved to GetAssistancePhonesPsaExecutor class")
internal class GetRoadSideContactPSAExecutor(command: BaseCommand) :
    BasePsaExecutor<String, Map<String, Map<String, String>>?>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<Map<String, Map<String, String>>?> {
        return GetSettingsPSAExecutor(middlewareComponent, params).execute()
            .map { settings ->
                val phones = mutableMapOf<String, String>()
                    .apply {
                        settings.assistanceRsaPhone
                            .takeIf { !it.isNullOrBlank() }
                            ?.let { put(Constants.PRIMARY, it) }
                    }
                    .takeIf { it.isNotEmpty() }
                    .orEmpty()
                mapOf(Constants.Output.Common.PHONES to phones)
            }
    }
}
