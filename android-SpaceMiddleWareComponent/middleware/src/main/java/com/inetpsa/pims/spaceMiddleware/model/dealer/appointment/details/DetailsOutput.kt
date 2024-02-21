package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Reminder
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status

internal data class DetailsOutput(
    @SerializedName("id") val id: String? = null,
    @SerializedName("vin") val vin: String? = null,
    @SerializedName("bookingId") val bookingId: String? = null,
    @SerializedName("bookingLocation") val bookingLocation: String? = null,
    @SerializedName("scheduledTime") val scheduledTime: String? = null,
    @SerializedName("comment") val comment: String? = null,
    @SerializedName("mileage") val mileage: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("status") val status: Status? = null,
    @SerializedName("services") val services: List<Service>? = null,
    @SerializedName("amount") val amount: Float? = null,
    @SerializedName("reminders") val reminders: List<Reminder>? = null
) {

    @Keep
    internal data class Service(
        @SerializedName("id") val id: String? = null,
        @SerializedName("comment") val comment: String? = null,
        @SerializedName("type") val type: ServiceType? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("opCode") val opCode: String? = null,
        @SerializedName("price") val price: Float? = null
    )
}
