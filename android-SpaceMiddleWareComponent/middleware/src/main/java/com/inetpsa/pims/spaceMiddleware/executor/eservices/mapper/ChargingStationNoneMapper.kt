package com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper

import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Location
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.FilterInput

internal class ChargingStationNoneMapper : ChargingStationMapper {

    override fun transformParamsToInput(params: Map<String, Any>?): FilterInput? = null

    override fun transformToBodyRequest(input: ChargeStationLocatorInput): ChargeStationLocatorFCARequest =
        ChargeStationLocatorFCARequest(
            location = Location(
                latitude = input.latitude,
                longitude = input.longitude
            )
        )
}
