package com.inetpsa.pims.spaceMiddleware.model.assistance

import com.google.gson.annotations.SerializedName

data class FaqOutput(
    @SerializedName("url") val url: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("email") val email: String? = null
)
