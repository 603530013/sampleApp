package com.inetpsa.pims.spaceMiddleware.model.vehicles.details

@Deprecated("We should switch to use GetUserResponse")
internal data class ServicePsa(
    val id: String,
    val title: String? = null,
    val description: String? = null,
    val category: String? = null,
    val url: String? = null,
    val urlSso: String? = null,
    val urlCvs: String? = null,
    val price: Double? = null,
    val currency: String? = null,
    val offer: Offer? = null
) {

    internal data class Offer(
        val id: Long = -1,
        val pricingModel: String,
        val price: Price
    ) {

        internal data class Price(
            val fkOffer: Long = -1,
            val periodType: String? = null,
            val price: Double,
            val currency: String
        )
    }
}
