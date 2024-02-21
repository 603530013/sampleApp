package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history

import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status

internal data class HistoryOutput(
    @SerializedName("appointments")
    val appointments: List<Appointment>? = null
) {

    internal data class Appointment(
        @SerializedName("id")
        val appointmentId: String? = null,
        @SerializedName("scheduledTime")
        val scheduledTime: String? = null,
        @SerializedName("status")
        val status: Status? = null
    )
}
