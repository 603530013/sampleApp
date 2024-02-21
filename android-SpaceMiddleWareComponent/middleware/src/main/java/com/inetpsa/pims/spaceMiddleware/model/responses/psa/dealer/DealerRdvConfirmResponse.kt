package com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer

import com.google.gson.annotations.SerializedName

internal data class DealerRdvConfirmResponse(
    @SerializedName("vin") val vin: String? = null,
    @SerializedName("rid") val rid: String? = null,
    @SerializedName("account_id") val accountId: String? = null,
    @SerializedName("sitegeo") val siteGeo: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("rrdi") val rrdi: String? = null,
    @SerializedName("day") val day: String? = null,
    @SerializedName("hour") val hour: String? = null,
    @SerializedName("contact") val contact: Int? = null,
    @SerializedName("mobility") val mobility: Int? = null,
    @SerializedName("total") val total: Int? = null,
    @SerializedName("discount") val discount: Int? = null,
    @SerializedName("created") val created: String? = null,
    @SerializedName("basket_id") val basketId: String? = null,
    @SerializedName("operations") val operations: List<Operations>?
) {

    internal data class Operations(
        @SerializedName("reference") val reference: String?,
        @SerializedName("icon") val icon: String? = null,
        @SerializedName("title") val title: String?,
        @SerializedName("type") val type: Int?,
        @SerializedName("is_package") val isPackage: Int?,
        @SerializedName("packages") val packages: List<Packages>? = null,
        @SerializedName("intervention_label") val interventionLabel: String? = null
    )

    internal data class Packages(
        @SerializedName("reference") val reference: String? = null,
        @SerializedName("title") val title: String?,
        @SerializedName("price") val price: Int? = null,
        @SerializedName("type") val type: Int?
    )
}
