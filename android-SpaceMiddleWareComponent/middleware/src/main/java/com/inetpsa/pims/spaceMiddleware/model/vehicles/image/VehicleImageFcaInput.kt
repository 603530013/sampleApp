package com.inetpsa.pims.spaceMiddleware.model.vehicles.image

internal data class VehicleImageFcaInput(
    val vin: String,
    val width: Int = 300,
    val height: Int = 300,
    val imageFormat: String = "png"
)
