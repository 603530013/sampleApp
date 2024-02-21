package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.BookingIdField

internal data class HistoryNaftaInput(
    override var dealerId: String,
    val vin: String
) : BookingIdField
