package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.delete

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.BookingIdField

internal data class DeleteNaftaLatamInput(
    val vin: String,
    val id: String,
    override var dealerId: String
) : BookingIdField
