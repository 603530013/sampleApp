package com.inetpsa.pims.spaceMiddleware.executor.eservices.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.CslProvider.BOSCH
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.CslProvider.F2M
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.CslProvider.TOMTOM
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.ChargeStationFiltersOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.ChargeStationFiltersOutput.FilterItem
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.ChargingStationFilters

internal class GetChargingStationFiltersExecutor(command: BaseCommand) :
    BaseFcaExecutor<String, ChargeStationFiltersOutput?>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<ChargeStationFiltersOutput?> {
        val partner = MarketPlacePartnerManager().fetchChargingStationLocator(middlewareComponent, params, input)
        val vehicle = CachedVehicles.getOrThrow(middlewareComponent, input)
        val filters = transformToFilters(vehicle.cslProvider)
        val response = filters?.let { ChargeStationFiltersOutput(filters = it, hasPartner = !partner.isNullOrEmpty()) }
        return NetworkResponse.Success(response)
    }

    private fun transformToFilters(provider: VehicleResponse.CslProvider?): List<FilterItem>? =
        when (provider) {
            F2M -> ChargingStationFilters.F2M.filters

            TOMTOM -> ChargingStationFilters.Tomtom.filters

            BOSCH -> ChargingStationFilters.Bosch.filters

            else -> null
        }
}
