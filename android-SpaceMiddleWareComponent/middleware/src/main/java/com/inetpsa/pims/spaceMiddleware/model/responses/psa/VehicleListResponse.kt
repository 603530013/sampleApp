package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName

internal data class VehicleListResponse(
    @SerializedName("vin") val vin: String,
    @SerializedName("lcdv") val lcdv: String,
    @SerializedName("short_label") val shortLabel: String,
    @SerializedName("visual") val visual: String?,
    // @SerializedName("warranty_start_date") val warrantyStartDate: Long?,
    @SerializedName("command") val command: String?
)
