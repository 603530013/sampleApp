package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

internal data class CreateXPSAInput(
    override val vin: String,
    override val date: LocalDateTime,
    override val bookingId: String,
    override val services: List<String>?,
    val mobility: Boolean?,
    val comment: String?,
    val phone: String?,
    val contact: String?,
    val premiumService: Service?
) : CreateInput {

    internal enum class Service {
        @SerializedName("Delivery")
        Delivery,

        @SerializedName("PickUp")
        PickUp,

        @SerializedName("Pick_Up_And_Delivery")
        PickUpAndDelivery,

        @SerializedName("Courstesy_Vehicle")
        CourtesyVehicle,

        @SerializedName("Mobility_Service")
        MobilityService,

        @SerializedName("NONE")
        NONE
    }
}
