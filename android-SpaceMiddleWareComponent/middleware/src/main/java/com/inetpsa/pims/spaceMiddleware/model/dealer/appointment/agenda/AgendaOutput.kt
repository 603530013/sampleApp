package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda

import com.google.gson.annotations.SerializedName

// TODO This model should be updated to be a common model between xP and xF response
internal data class AgendaOutput(

    @field:SerializedName("rrdi")
    val rrdi: String? = null,

    @field:SerializedName("period")
    val period: Int? = null,

    @field:SerializedName("from")
    val from: String? = null,

    @field:SerializedName("to")
    val to: String? = null,

    @field:SerializedName("type")
    val type: Int? = null,

    @field:SerializedName("days")
    val days: List<DaysItem>? = null
) {

    internal data class DaysItem(

        @field:SerializedName("date")
        val date: String? = null,

        @field:SerializedName("slots")
        val slots: List<SlotsItem>? = null
    ) {

        internal data class SlotsItem(

            @field:SerializedName("reception_total")
            val receptionTotal: Int? = null,

            @field:SerializedName("start")
            val start: String? = null,

            @field:SerializedName("discount")
            val discount: Float? = null,

            @field:SerializedName("end")
            val end: String? = null,

            @field:SerializedName("reception_available")
            val receptionAvailable: Int? = null,

            @field:SerializedName("serviceAdvisors")
            val serviceAdvisors: List<ServiceAdvisorsItem?>? = null,

            @field:SerializedName("transportationOptions")
            val transportationOptions: List<TransportationOptionsItem?>? = null

        ) {

            data class TransportationOptionsItem(

                @field:SerializedName("code")
                val code: String? = null,
                val description: String? = null
            )

            data class ServiceAdvisorsItem(

                @field:SerializedName("id")
                val id: Int? = null,
                val name: String? = null,
                val memberId: Int? = null
            )
        }
    }
}
