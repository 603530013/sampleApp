package com.inetpsa.pims.spaceMiddleware.model.servicelist

@Deprecated("This model should be replaced by ContractsOutput")
internal data class ServicesList(val services: List<ServiceItem>) {

    internal data class ServiceItem(
        val id: String? = null,

        val type: String? = null,

        val title: String? = null,

        val category: String? = null,

        val description: String? = null,

        val url: String? = null,

        val urlSso: String? = null,

        val urlCvs: String? = null,

        val price: Int? = null,

        val currency: String? = null,

        val offer: Offer? = null,

        val validity: Validity? = null,

        val status: Int? = null

    )

    internal data class Offer(
        val pricingModel: String? = null,

        val price: Price? = null

    )

    internal data class Price(
        val periodType: String? = null,

        val price: Int? = null,

        val currency: String? = null
    )
}
