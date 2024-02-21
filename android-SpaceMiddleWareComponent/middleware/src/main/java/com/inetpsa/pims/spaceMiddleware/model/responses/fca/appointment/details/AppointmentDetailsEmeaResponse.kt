package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse

internal data class AppointmentDetailsEmeaResponse(
    @field:SerializedName("vin")
    val vin: String? = null,

    @field:SerializedName("serviceId")
    val serviceId: String? = null,

    @field:SerializedName("dealerId")
    val dealerId: String? = null,

    @field:SerializedName("location")
    val location: String? = null,

    @field:SerializedName("scheduledTime")
    val scheduledTime: String? = null,

    @field:SerializedName("faultDescription")
    val faultDescription: String? = null,

    @field:SerializedName("servicesList")
    val servicesList: List<Service>? = null,

    @field:SerializedName("telephone")
    val telephone: String? = null,

    @field:SerializedName("vehicleKm")
    val vehicleKm: String? = null,

    @field:SerializedName("status")
    val status: AppointmentStatusResponse? = null,

    @field:SerializedName("reminders")
    val reminders: List<String>? = null
) {

    @Keep
    internal data class Service(val description: String)
}
