package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services

internal data class ServicesEmeaMaseratiInput(
    var dealerId: String,
    val dealerLocation: String? = null,
    val vin: String
)
