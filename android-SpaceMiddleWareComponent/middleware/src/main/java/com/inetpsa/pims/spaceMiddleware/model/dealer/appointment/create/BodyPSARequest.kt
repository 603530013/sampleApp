package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create

import com.google.gson.annotations.SerializedName

internal data class BodyPSARequest(
    @SerializedName("rdv") val rdv: RDV
) {

    internal data class RDV(
        @field:SerializedName("vin") val vin: String,
        @field:SerializedName("sitegeo") val bookingId: String,
        @field:SerializedName("day") val day: String,
        @field:SerializedName("hour") val hour: String,
        @field:SerializedName("discount") val discount: Float? = null,
        @field:SerializedName("total") val total: Float?,
        @field:SerializedName("comment") val comment: String?,
        @field:SerializedName("mobility") val mobility: Boolean?,
        @field:SerializedName("phones") val phones: List<Phone>? = null,
        @field:SerializedName("contact") val contact: String? = null,
        @field:SerializedName("valet_services") val valetService: Services? = null,
        @field:SerializedName("plp_premium_service") val plpPremiumService: Services? = null,
        @field:SerializedName("operations") val operation: List<Map<String, Any?>>?
    ) {

        internal data class Phone(@SerializedName("phone") val phone: String)

        internal enum class Services {
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
}
