package com.inetpsa.pims.spaceMiddleware.model.vehicles.filters

import com.google.gson.annotations.SerializedName

internal data class ChargeStationFiltersOutput(
    @SerializedName("filters")
    val filters: List<FilterItem>,
    @SerializedName("hasPartner")
    val hasPartner: Boolean
) {

    data class FilterItem(
        @SerializedName("key")
        val key: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("data")
        val data: List<String>? = null
    )
}
