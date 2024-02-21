package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import com.google.gson.annotations.SerializedName

internal data class VehiclesResponse(
    @SerializedName("userid") val userid: String,
    @SerializedName("version") val version: String,
    @SerializedName("vehicles") val vehicles: List<VehicleResponse>
)
