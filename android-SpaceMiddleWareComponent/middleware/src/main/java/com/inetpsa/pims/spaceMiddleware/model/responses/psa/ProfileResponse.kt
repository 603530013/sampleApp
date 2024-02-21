package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName

internal data class ProfileResponse(
    @SerializedName("id") val idClient: String,
    @SerializedName("email") val email: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("address1") val address1: String? = null,
    @SerializedName("address2") val address2: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("zip_code") val zipCode: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("civility") val civility: String? = null,
    @SerializedName("civility_code") val civilityCode: String? = null,
    @SerializedName("cpf") val cpf: String? = null,
    @SerializedName("rut") val rut: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("mobile") val mobile: String? = null,
    @SerializedName("mobile_pro") val mobilePro: String? = null
)
