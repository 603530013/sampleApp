package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse

@Keep
internal data class AppointmentDetailsNaftaResponse(
    @SerializedName("advisor") val advisor: Advisor? = null,
    @SerializedName("customer") val customer: Customer? = null,
    @SerializedName("customerConcernsInfo") val customerConcernsInfo: String? = null,
    @SerializedName("mileage") val mileage: Mileage? = null,
    @SerializedName("scheduledTime") val scheduledTime: Long? = null,
    @SerializedName("services") val services: Services? = null,
    @SerializedName("status") val status: AppointmentStatusResponse? = null,
    @SerializedName("transportationOption") val transportationOption: TransportationOption? = null
) {

    @Keep
    internal data class Advisor(
        @SerializedName("departmentId") val departmentId: Int? = null,
        @SerializedName("id") val id: Int? = null,
        @SerializedName("name") val name: String? = null
    )

    @Keep
    internal data class Customer(
        @SerializedName("firstName") val firstName: String? = null,
        @SerializedName("id") val id: String? = null,
        @SerializedName("lastName") val lastName: String? = null
    )

    @Keep
    internal data class Mileage(
        @SerializedName("unitsKind") val unitsKind: String? = null,
        @SerializedName("value") val value: Float? = null
    )

    @Keep
    internal data class Services(
        @SerializedName("drs") val drs: List<Service>? = null,
        @SerializedName("frs") val frs: List<Service>? = null,
        @SerializedName("recalls") val recalls: List<Service>? = null,
        @SerializedName("repair") val repair: List<Service>? = null,
        @SerializedName("summary") val summary: Summary? = null
    ) {

        @Keep
        internal data class Service(
            @SerializedName("comment") val comment: String? = null,
            @SerializedName("id") val id: Int? = null,
            @SerializedName("name") val name: String? = null,
            @SerializedName("price") val price: Float? = null,
            @SerializedName("selected") val selected: Boolean = false
        )

        @Keep
        internal data class Summary(
            @SerializedName("subTotal") val subTotal: Float? = null,
            @SerializedName("taxes") val taxes: Float? = null,
            @SerializedName("total") val total: Float? = null
        )
    }

    @Keep
    internal data class TransportationOption(
        @SerializedName("code") val code: String? = null,
        @SerializedName("description") val description: String? = null
    )
}
