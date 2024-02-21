package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment

import com.google.gson.annotations.SerializedName

internal data class DealerAgendaMaseratiResponse(
    @SerializedName("dealerId") val dealerId: String,
    @SerializedName("agenda") val agenda: List<Agenda>?
) {

    internal data class Agenda(
        @SerializedName("dateString") val date: String?, // DD-MM-YYYY
        @SerializedName("slots") val slots: List<Slot>?
    ) {

        internal data class Slot(
            @SerializedName("timeString") val time: String // hh:mm
        )
    }
}
