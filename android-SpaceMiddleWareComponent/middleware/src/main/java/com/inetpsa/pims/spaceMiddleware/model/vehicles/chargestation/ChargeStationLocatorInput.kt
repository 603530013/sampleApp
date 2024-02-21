package com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation

import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.FilterInput

internal data class ChargeStationLocatorInput(
    val vin: String,
    val longitude: Double,
    val latitude: Double,
    var filters: FilterInput? = null
)
