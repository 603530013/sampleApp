package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create

import java.time.LocalDateTime

internal data class CreateEMEAInput(
    override val vin: String,
    override val date: LocalDateTime,
    override val bookingId: String,
    override val services: List<String>?,
    val mileage: Int?,
    val codNation: String?, // IT or EN
    val comment: String?,
    val bookingLocation: String?, // EMEA ONLY REQUIRED
    val contactName: String?,
    val contactPhone: String?
) : CreateInput
