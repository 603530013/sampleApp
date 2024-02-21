package com.inetpsa.pims.spaceMiddleware.model.vehicles.remove

internal data class RemoveVehicleInput(
    val vin: String,
    val reason: String?,
    val reasonId: String?
)
