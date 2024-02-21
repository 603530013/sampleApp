package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create

internal data class CachedAppointmentsXPSA(val appointments: HashSet<CachedAppointmentXPSA>) {
    internal data class CachedAppointmentXPSA(
        val appointmentId: String? = null,
        val status: String? = null,
        val date: String? = null
    )
}
