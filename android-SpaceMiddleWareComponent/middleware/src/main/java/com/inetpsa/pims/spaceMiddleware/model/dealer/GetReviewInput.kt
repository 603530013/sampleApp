package com.inetpsa.pims.spaceMiddleware.model.dealer

internal data class GetReviewInput(
    val vin: String,
    val vehicleIdType: String,
    val serviceType: String
)
