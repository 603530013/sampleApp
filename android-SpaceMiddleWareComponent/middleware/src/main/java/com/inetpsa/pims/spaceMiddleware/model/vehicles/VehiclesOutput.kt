package com.inetpsa.pims.spaceMiddleware.model.vehicles

internal data class VehiclesOutput(val vehicles: List<Vehicle>) {
    internal data class Vehicle(
        val vin: String,
        val shortLabel: String?,
        val modelDescription: String? = null,
        val picture: String?
    )
}
