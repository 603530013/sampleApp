package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
internal data class VehicleDetailsResponse(
    @SerializedName("vin") val vin: String,
    @SerializedName("lcdv") val lcdv: String,
    @SerializedName("visual") val visual: String?,
    @SerializedName("short_label") val shortName: String,
    // @SerializedName("warranty_start_date") val warrantyStartDate: Long?,
    @SerializedName("attributes") val attributes: List<String>,
    @SerializedName("eligibility") val eligibility: List<String>,
    @SerializedName("type_vehicle") val typeVehicle: Int,
    @SerializedName("mileage") val mileage: Mileage,
    @SerializedName("review_max_date") val reviewMaxDate: Long?,
    @SerializedName("services_connected") val servicesConnected: List<ServicesConnected>?
) : Parcelable {

    companion object {

        internal const val VEHICLE_THERMIC: Int = 0
        internal const val VEHICLE_HYBRID_A: Int = 2
        internal const val VEHICLE_HYBRID_B: Int = 3
        internal const val VEHICLE_ELECTRIC: Int = 4
        internal const val VEHICLE_HYBRID_C: Int = 5
    }

    @Keep
    @Parcelize
    internal data class Mileage(

        @SerializedName("value") val value: Int,
        @SerializedName("timestamp") val timestamp: Long,
        @SerializedName("source") val source: Int
    ) : Parcelable

    @Keep
    @Parcelize
    internal data class ServicesConnected(

        @SerializedName("id") val id: String,
        @SerializedName("title") val title: String,
        @SerializedName("category") val category: String,
        @SerializedName("description") val description: String,
        @SerializedName("url") val url: String,
        @SerializedName("urlSso") val urlSso: String,
        @SerializedName("urlCvs") val urlCvs: String,
        @SerializedName("price") val price: Float,
        @SerializedName("currency") val currency: String,
        @SerializedName("offer") val offer: Offer?
    ) : Parcelable {

        @Keep
        @Parcelize
        internal data class Offer(

            @SerializedName("pricingModel") val pricingModel: String?,
            @SerializedName("fromPrice") val fromPrice: Float?,
            @SerializedName("price") val price: Price?,
            @SerializedName("isFreetrial") val isFreeTrial: Int?
        ) : Parcelable {

            @Keep
            @Parcelize
            internal data class Price(

                @SerializedName("periodType") val periodType: String?,
                @SerializedName("price") val price: Float?,
                @SerializedName("currency") val currency: String?,
                @SerializedName("typeDiscount") val typeDiscount: String?
            ) : Parcelable
        }
    }
}
