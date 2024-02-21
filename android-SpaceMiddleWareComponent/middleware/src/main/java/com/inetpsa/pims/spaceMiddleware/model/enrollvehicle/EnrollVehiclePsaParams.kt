package com.inetpsa.pims.spaceMiddleware.model.enrollvehicle

@Deprecated("should replace with VehicleInput")
internal data class EnrollVehiclePsaParams(
    val vin: String,
    val mileage: Int?
)
