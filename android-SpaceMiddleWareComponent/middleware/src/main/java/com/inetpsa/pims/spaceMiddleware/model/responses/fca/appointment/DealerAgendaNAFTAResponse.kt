package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment

import com.google.gson.annotations.SerializedName

internal data class DealerAgendaNAFTAResponse(
    @SerializedName("segments") val segments: List<Segment>? = null
) {

    data class Segment(

        @field:SerializedName("date")
        val date: Long? = null,

        @field:SerializedName("slots")
        val slots: List<Slot?>? = null
    ) {

        data class Slot(

            @field:SerializedName("transportationOptions")
            val transportationOptions: List<TransportationOptionsItem?>? = null,

            @field:SerializedName("time")
            val time: Long? = null,

            @field:SerializedName("serviceAdvisors")
            val serviceAdvisors: List<ServiceAdvisorsItem?>? = null
        ) {

            data class TransportationOptionsItem(

                @field:SerializedName("code")
                val code: String? = null
            )

            data class ServiceAdvisorsItem(

                @field:SerializedName("id")
                val id: Int? = null
            )
        }
    }
}
