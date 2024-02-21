package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
internal data class AppointmentDetailsLatamResponse(
    @SerializedName("customer") val customer: Customer? = null,
    @SerializedName("licensePlate") val licensePlate: String? = null,
    @SerializedName("mileage") val mileage: Mileage? = null,
    @SerializedName("reminders") val reminders: List<String>? = null,
    @SerializedName("scheduledTime") val scheduledTime: Long? = null,
    @SerializedName("services") val services: Services? = null,
    @SerializedName("status") val status: AppointmentStatusResponse? = null
) : Parcelable {

    @Keep
    @Parcelize
    internal data class Customer(
        @SerializedName("email") val email: String? = null,
        @SerializedName("firstName") val firstName: String? = null,
        @SerializedName("lastName") val lastName: String? = null
    ) : Parcelable

    @Keep
    @Parcelize
    internal data class Mileage(
        @SerializedName("unitsKind") val unitsKind: String? = null,
        @SerializedName("value") val value: Double? = null
    ) : Parcelable

    @Keep
    @Parcelize
    internal data class Services(
        @SerializedName("drs") val drs: List<Service>? = null,
        @SerializedName("frs") val frs: List<Service>? = null,
        @SerializedName("recalls") val recalls: List<Service>? = null,
        @SerializedName("repair") val repair: List<Service>? = null
    ) : Parcelable {

        @Keep
        @Parcelize
        internal data class Service(
            @SerializedName("comment") val comment: String? = null,
            @SerializedName("id") val id: Int? = null,
            @SerializedName("name") val name: String? = null,
            @SerializedName("opCode") val opCode: String? = null,
            @SerializedName("price") val price: Float? = null,
            @SerializedName("selected") val selected: Boolean? = false
        ) : Parcelable
    }
}
