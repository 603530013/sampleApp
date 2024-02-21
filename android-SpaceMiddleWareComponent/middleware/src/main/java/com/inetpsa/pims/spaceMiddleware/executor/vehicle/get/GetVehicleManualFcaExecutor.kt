package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand.MASERATI
import com.inetpsa.mmx.foundation.tools.Market.NAFTA
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.vehicles.manual.OwnerManualOutput
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.map
import java.util.Locale

internal class GetVehicleManualFcaExecutor(
    command: BaseCommand
) : BaseFcaExecutor<String, OwnerManualOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<OwnerManualOutput> {
        val usCountry = Locale.US.country
        val vehicle = CachedVehicles.getOrThrow(middlewareComponent, input)
        val vehicleCountry = vehicle.market.orEmpty()
        val vehicleBrand = vehicle.subMake.orEmpty()

        return when {
            vehicleBrand.equals(MASERATI.name, ignoreCase = true) -> throw PimsErrors.apiNotSupported()

            vehicleCountry.equals(usCountry, ignoreCase = true) && configurationManager.market == NAFTA ->
                GetCountryOwnerManualFcaExecutor(middlewareComponent, params)
                    .execute(vehicle)
                    .map { pdfLink ->
                        when (pdfLink) {
                            null -> OwnerManualOutput()
                            else -> OwnerManualOutput(
                                type = OwnerManualOutput.Type.PDF.value,
                                url = pdfLink
                            )
                        }
                    }

            else -> GetMarketOwnerManualFcaExecutor(middlewareComponent, params)
                .execute(input)
                .map { items ->
                    OwnerManualOutput(
                        type = OwnerManualOutput.Type.Web.value,
                        url = items
                    )
                }
        }
    }
}
