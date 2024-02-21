package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history

import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse

internal data class AppointmentHistoryLATAMResponse(
    @field:SerializedName("appointments")
    val appointments: List<Appointment>? = null
) {

    internal data class Appointment(
        @field:SerializedName("dealerId")
        val dealerId: String? = null,

        @field:SerializedName("appointmentId")
        val appointmentId: String? = null,

        @field:SerializedName("scheduledTime")
        val scheduledTime: Long? = null,

        @field:SerializedName("status")
        val status: AppointmentStatusResponse? = null
    )
}
