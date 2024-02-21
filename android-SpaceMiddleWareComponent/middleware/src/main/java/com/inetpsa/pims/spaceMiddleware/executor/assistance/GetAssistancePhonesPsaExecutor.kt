package com.inetpsa.pims.spaceMiddleware.executor.assistance

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetSettingsPSAExecutor
import com.inetpsa.pims.spaceMiddleware.model.assistance.CallCenter
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.SettingsCallCenterItemResponse.CallCenterSettingFca
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetAssistancePhonesPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<String, Map<String, CallCenter>>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.Input.VIN

    override suspend fun execute(input: String): NetworkResponse<Map<String, CallCenter>> {
        return GetSettingsPSAExecutor(middlewareComponent, params).execute()
            .map { settings ->
                mutableMapOf<String, CallCenter>()
                    .apply {
                        settings.assistanceRsaPhone
                            .takeIf { !it.isNullOrBlank() && settings.assistanceRsaEnable == true }
                            ?.let {
                                put(
                                    CallCenterSettingFca.CallCenterType.RoadSide.output,
                                    CallCenter(primary = it)
                                )
                            }

                        settings.assistanceBrandPhone
                            .takeIf { !it.isNullOrBlank() }
                            ?.let {
                                put(
                                    CallCenterSettingFca.CallCenterType.Brand.output,
                                    CallCenter(primary = it)
                                )
                            }

                        settings.o2xPhoneSOS
                            .takeIf { !it.isNullOrBlank() && settings.o2xEnable == true }
                            ?.let {
                                put(
                                    CallCenterSettingFca.CallCenterType.Emergency.output,
                                    CallCenter(primary = it)
                                )
                            }
                    }
            }
    }
}
