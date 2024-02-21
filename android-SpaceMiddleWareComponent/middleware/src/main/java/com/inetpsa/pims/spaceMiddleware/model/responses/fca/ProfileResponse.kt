package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import com.google.gson.annotations.SerializedName

internal data class ProfileResponse(
    @SerializedName("uid") val uid: String,

    @SerializedName("address") val address: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("zip") val zip: String? = null,

    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,

    @SerializedName("locale") val locale: String? = null,

    // The gender of the user. Can be 'm', 'f', or 'u', for male, female, or unspecified.
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("phones") val phones: List<Phone>? = null,
    @SerializedName("photoURL") val photoURL: String? = null
) {

    internal data class Phone(val number: String? = null, val type: String? = null)
}
