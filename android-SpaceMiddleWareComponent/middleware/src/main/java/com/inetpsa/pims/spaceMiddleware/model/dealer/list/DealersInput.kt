package com.inetpsa.pims.spaceMiddleware.model.dealer.list

internal data class DealersInput(
    val vin: String,
    val latitude: Double,
    val longitude: Double,
    val max: Int?
)
