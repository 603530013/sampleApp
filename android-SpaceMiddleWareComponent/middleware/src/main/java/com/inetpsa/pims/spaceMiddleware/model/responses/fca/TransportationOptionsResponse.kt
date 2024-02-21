package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import com.google.gson.annotations.SerializedName

internal data class TransportationOptionsResponse(
    @field:SerializedName("transportationOptions")
    val transportations: List<TransportationOption>? = null
) {

    internal data class TransportationOption(
        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("description")
        val description: String? = null
    )
}
