package com.inetpsa.pims.spaceMiddleware.model.vehicles.list

import com.google.gson.annotations.SerializedName

@Deprecated("We should switch to use VehiclesOutput")
internal data class VehicleListPsa(val vehicleList: List<VehiclePsa>) {
    internal data class VehiclePsa(
        val command: String? = null,
        val vin: String? = null,
        val lcdv: String? = null,
        @SerializedName("short_label") val shortLabel: String? = null
    )
}
