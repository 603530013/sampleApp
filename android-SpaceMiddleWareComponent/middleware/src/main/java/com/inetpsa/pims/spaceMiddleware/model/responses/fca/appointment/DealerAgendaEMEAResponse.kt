package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment

import com.google.gson.annotations.SerializedName

internal data class DealerAgendaEMEAResponse(
    @SerializedName("brand") val brand: String,
    @SerializedName("location") val location: String?,
    @SerializedName("dealerId") val dealerId: String,
    @SerializedName("agenda") val agenda: List<Agenda>? = null
) {

    internal data class Agenda(
        @SerializedName("date") val date: String?, // timestamp
        @SerializedName("slots") val slots: List<Slot>?
    ) {

        val availableSlot: List<Slot>?
            get() = slots?.filter {
                // We could receive also "-" as slot meaning for that time there is an unlimited number of slots
                it.slot.equals("-") || (it.slot?.toIntOrNull() ?: 0) > 0
            }

        internal data class Slot(
            @SerializedName("time") val time: String?, // timestamp
            @SerializedName("limit") val limit: String?,
            @SerializedName("reservation") val reservation: String?,
            @SerializedName("slot") val slot: String?
        )
    }
}
