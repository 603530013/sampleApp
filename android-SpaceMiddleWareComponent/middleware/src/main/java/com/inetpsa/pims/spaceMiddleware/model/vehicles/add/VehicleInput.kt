package com.inetpsa.pims.spaceMiddleware.model.vehicles.add

internal data class VehicleInput(val vin: String, val mileage: Int? = null, val connected: Boolean = false)
