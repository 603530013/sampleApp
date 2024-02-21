package com.inetpsa.pims.spaceMiddleware.model.destination

internal data class DestinationInput(
    val vin: String,
    val provider: PoiInfoProvider,
    val destination: Destination
)
