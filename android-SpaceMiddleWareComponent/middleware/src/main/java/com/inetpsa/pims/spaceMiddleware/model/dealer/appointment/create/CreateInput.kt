package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create

import java.time.LocalDateTime

internal interface CreateInput {
    val vin: String
    val date: LocalDateTime
    val bookingId: String
    val services: List<String>?
}
