package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment

import com.google.gson.annotations.SerializedName

internal data class DealerAgendaLATAMResponse(
    @SerializedName("segments") val segments: List<Segment>? = null
) {

    internal data class Segment(
        @SerializedName("date") val date: Long? = null,
        @SerializedName("slots") val slots: List<Slot>? = null
    ) {

        internal data class Slot(
            @SerializedName("time") val time: Long? = null,
            @SerializedName("slotId") val slotId: Int? = null,
            @SerializedName("transportationOptions") val transportationOptions: List<TransportationOption>? = null
        ) {

            internal data class TransportationOption(
                @SerializedName("code") val code: String? = null
            )
        }
    }
}
