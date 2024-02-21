package com.inetpsa.pims.spaceMiddleware.model.user

import com.google.gson.annotations.SerializedName

data class ProfileInput(
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("civility") val civility: String? = null,
    @SerializedName("address1") val address1: String? = null,
    @SerializedName("address2") val address2: String? = null,
    @SerializedName("zip_code") val zipCode: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("mobile") val mobile: String? = null,
    @SerializedName("mobile_pro") val mobilePro: String? = null
)
