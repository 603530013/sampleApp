package com.inetpsa.pims.spaceMiddleware.model.vehicles.service

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
internal data class ServicesOutput(val services: List<Service>?) : Parcelable {

    @Keep
    @Parcelize
    data class Service(
        val id: String,
        val url: String?,
        val title: String?,
        val category: String?,
        val description: String?,
        val price: Float?,
        val currency: String?,
        val offer: Offer?
    ) : Parcelable {

        @Keep
        @Parcelize
        data class Offer(

            val pricingModel: String?,
            val fromPrice: Float?,
            val price: Price?,
            val isFreeTrial: Int?
        ) : Parcelable {

            @Keep
            @Parcelize
            data class Price(
                val periodType: String?,
                val price: Float?,
                val currency: String?,
                val typeDiscount: String?
            ) : Parcelable
        }
    }
}
