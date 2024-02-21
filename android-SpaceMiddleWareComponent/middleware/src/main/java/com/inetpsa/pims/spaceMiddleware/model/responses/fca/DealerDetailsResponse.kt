package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import com.google.gson.annotations.SerializedName

internal data class DealerDetailsResponse(
    @SerializedName("dealerId") val id: String?,
    val name: String?,
    val address: String?,
    val phoneNumber: String?,
    val ossDealerId: String?,
    val website: String?,
    val latitude: String?,
    val longitude: String?,
    val preferred: Boolean?,
    val coupons: Coupon?,
    val serviceScheduling: Boolean?,
    @SerializedName("dealerServices") val services: List<Services>?,
    val departments: Map<String, Map<String, OpeningHours?>?>?
) {

    internal data class OpeningHours(
        val closed: Boolean?,
        val hour24: Boolean?,
        val open: HourIndication?,
        val close: HourIndication?
    ) {

        internal data class HourIndication(
            val time: String?,
            val ampm: String?
        )
    }

    internal data class Coupon(
        val category: String?,
        val couponId: String?,
        val includes: String?,
        val dealerId: String?,
        val header: String?,
        val expires: Double?,

        val priceHeader1: String?,
        val price1: String?,
        val postPrice1: String?,

        val priceHeader2: String?,
        val price2: String?,
        val postPrice2: String?,

        val priceHeader3: String?,
        val price3: String?,
        val postPrice3: String?,

        val priceHeader4: String?,
        val price4: String?,
        val postPrice4: String?,

        val imageURL: String?,
        val url: String?
    )

    internal data class Services(
        val servicesForBrand: String?,
        val services: List<String>?,
        val hasBusinessLink: Boolean = false,
        val hasShuttle: Boolean = false,
        val hasCertifiedPreOwnedVehicles: Boolean = false,
        val hasExpressLube: Boolean = false,
        val hasServiceContract: Boolean = false,
        val hasMoparAccessories: Boolean = false,
        val hasSaturdayService: Boolean = false,
        val hasRentalService: Boolean = false,
        val hasSpanishPersonnel: Boolean = false,
        val isCustomerFirst: Boolean = false,
        val isSupportingOSS: Boolean = false,
        val isSupportingPUDO: Boolean = false,
        val hasCertifiedWagoneer: Boolean = false
    )
}
