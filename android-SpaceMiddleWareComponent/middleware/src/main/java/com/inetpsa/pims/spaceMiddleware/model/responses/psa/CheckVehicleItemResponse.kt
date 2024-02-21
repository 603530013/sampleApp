package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName

internal data class CheckVehicleItemResponse(
    // @field:SerializedName("short_label") val shortLabel: String?,
    // @field:SerializedName("warranty_start_date") val warrantyStartDate: Int?,
    // @field:SerializedName("visual") val visual: String?,
    @field:SerializedName("vin") val vin: String
    // @field:SerializedName("lcdv") val lcdv: String?,
    // @field:SerializedName("command") val command: String?
)
