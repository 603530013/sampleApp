package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import com.google.gson.annotations.SerializedName

internal data class AppointmentDetailsResponse(
    @field:SerializedName("advisor") val advisor: Advisor? = null,
    @field:SerializedName("customer") val customer: Customer? = null,
    @field:SerializedName("customerConcernsInfo") val customerConcernsInfo: String? = null,
    @field:SerializedName("mileage") val mileage: Mileage? = null,
    @field:SerializedName("reminders") val reminders: List<Long?>? = null,
    @field:SerializedName("scheduledTime") val scheduledTime: Long? = null,
    @field:SerializedName("services") val services: Services? = null,
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("transportationOption") val transportationOption: TransportationOption? = null
) {
    internal data class Advisor(
        @field:SerializedName("departmentId") val departmentId: Int? = null,
        @field:SerializedName("id") val id: Int? = null,
        @field:SerializedName("name") val name: String? = null
    )
    internal data class Customer(
        @field:SerializedName("firstName") val firstName: String? = null,
        @field:SerializedName("id") val id: String? = null,
        @field:SerializedName("lastName") val lastName: String? = null
    )
    internal data class Mileage(
        @field:SerializedName("unitsKind") val unitsKind: String? = null,
        @field:SerializedName("value") val value: Float? = null
    )
    internal data class Services(
        @field:SerializedName("drs") val drs: List<Any?>? = null,
        @field:SerializedName("frs") val frs: List<Fr?>? = null,
        @field:SerializedName("recalls") val recalls: List<Any?>? = null,
        @field:SerializedName("repair") val repair: List<Any?>? = null,
        @field:SerializedName("summary") val summary: Summary? = null
    ) {
        internal data class Fr(
            @field:SerializedName("comment") val comment: String? = null,
            @field:SerializedName("id") val id: Int? = null,
            @field:SerializedName("name") val name: String? = null,
            @field:SerializedName("price") val price: Double? = null,
            @field:SerializedName("selected") val selected: Boolean? = null
        )
        internal data class Summary(
            @field:SerializedName("subTotal") val subTotal: Double? = null,
            @field:SerializedName("taxes") val taxes: Double? = null,
            @field:SerializedName("total") val total: Double? = null
        )
    }
    internal data class TransportationOption(
        @field:SerializedName("code") val code: String? = null,
        @field:SerializedName("description") val description: String? = null
    )
}
