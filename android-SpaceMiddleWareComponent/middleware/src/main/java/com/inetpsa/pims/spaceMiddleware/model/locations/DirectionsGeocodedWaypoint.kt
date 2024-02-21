package com.inetpsa.pims.spaceMiddleware.model.locations

import com.google.gson.annotations.SerializedName

internal data class DirectionsGeocodedWaypoint(
    @SerializedName("geocoder_status") val geocoderStatus: String,
    @SerializedName("place_id") val placeId: String,
    @SerializedName("types") val types: List<String>
)
