package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_BRAND
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetSettingsPSAExecutor
import com.inetpsa.pims.spaceMiddleware.model.vehicles.manual.OwnerManualOutput
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetVehicleManualPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<String, OwnerManualOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<OwnerManualOutput> =
        when (configurationManager.brand) {
            Brand.PEUGEOT, Brand.CITROEN, Brand.DS -> {
                val response = OwnerManualOutput(type = OwnerManualOutput.Type.SDK.value)
                NetworkResponse.Success(response)
            }

            Brand.OPEL, Brand.VAUXHALL -> GetSettingsPSAExecutor(middlewareComponent, params).execute()
                .map { response ->
                    val link = response.ownerManualWebViewUrl
                    when (link?.isNotBlank()) {
                        true -> OwnerManualOutput(
                            type = OwnerManualOutput.Type.Web.value,
                            url = response.ownerManualWebViewUrl + input
                        )

                        else -> OwnerManualOutput()
                    }
                }

            else -> throw PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)
        }
}
