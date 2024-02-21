package com.inetpsa.pims.spaceMiddleware.model.settings

import com.google.gson.annotations.SerializedName

internal data class PaymentListOutput(
    @SerializedName("url") val url: String? = null
)
