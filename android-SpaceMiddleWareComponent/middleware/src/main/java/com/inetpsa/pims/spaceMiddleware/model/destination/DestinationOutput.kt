package com.inetpsa.pims.spaceMiddleware.model.destination

import com.google.gson.annotations.SerializedName

internal data class DestinationOutput(
    @SerializedName("command") val command: String,
    @SerializedName("correlationId") val correlationId: String,
    @SerializedName("responseStatus") val responseStatus: String,
    @SerializedName("statusTimestamp") val statusTimestamp: Long
)
