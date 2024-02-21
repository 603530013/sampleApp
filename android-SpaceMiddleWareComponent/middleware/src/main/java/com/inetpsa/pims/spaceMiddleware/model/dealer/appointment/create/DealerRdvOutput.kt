package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create

internal data class DealerRdvOutput(
    val vin: String? = null,
    val bookingId: String? = null, // booking ID
    val day: String? = null,
    val hour: String? = null, // day and hour is the same as scheduletime
    val contact: Int? = null, // telephone
    val mobility: Int? = null, // to be checked
    val discount: Int? = null, //
    val appointmentId: String? = null, // basketId
    val operations: List<Operations>?
) {

    internal data class Operations( // services
        val reference: String?, // code
        val title: String?,
        val type: Int?,
        val isPackage: Int?,
        val interventionLabel: String? = null
    )
}
