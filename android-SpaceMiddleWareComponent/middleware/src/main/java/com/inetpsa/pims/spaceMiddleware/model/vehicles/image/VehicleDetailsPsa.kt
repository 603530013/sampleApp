package com.inetpsa.pims.spaceMiddleware.model.vehicles.image

import com.google.gson.annotations.SerializedName

internal data class VehicleDetailsPsa(
    @SerializedName("vin") val vin: String?,
    @SerializedName("lcdv") val lcdv: String?,
    @SerializedName("short_label") val shortLabel: String?,
    @SerializedName("warranty_start_date") val warrantyStartDate: Int?,
    @SerializedName("type_vehicle") val typeVehicle: Int?,
    @SerializedName("visual") val visual: String?,
    @SerializedName("review_max_date") val reviewMaxDate: Int?
)
