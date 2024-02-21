package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.BookingIdField

internal data class DetailsNaftaInput(
    val id: String,
    val vin: String,
    override var dealerId: String
) : BookingIdField
