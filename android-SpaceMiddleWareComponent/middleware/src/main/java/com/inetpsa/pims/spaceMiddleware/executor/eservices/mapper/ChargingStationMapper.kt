package com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper

import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.FilterInput

internal interface ChargingStationMapper {

    /**
     * Transform params to input
     *
     * @param params
     * @return
     */
    fun transformParamsToInput(params: Map<String, Any>?): FilterInput?

    /**
     * Transform to body request
     *
     * @param input
     * @return
     */
    fun transformToBodyRequest(input: ChargeStationLocatorInput): ChargeStationLocatorFCARequest
}
