package com.inetpsa.pims.spaceMiddleware.model.destination

import com.google.gson.annotations.SerializedName

internal data class VehicleDestinationResponseFca(
    @SerializedName("command") val command: String,
    @SerializedName("correlationId") val correlationId: String,
    @SerializedName("error") val error: Error?,
    @SerializedName("responseStatus") val responseStatus: String,
    @SerializedName("statusTimestamp") val statusTimestamp: Long,
    @SerializedName("asyncRespTimeout") val asyncRespTimeout: Int
) {
    internal data class Error(
        @SerializedName("code") val code: String,
        @SerializedName("message") val message: String
    )
}
