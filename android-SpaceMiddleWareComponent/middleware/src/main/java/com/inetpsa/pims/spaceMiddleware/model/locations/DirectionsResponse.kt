package com.inetpsa.pims.spaceMiddleware.model.locations

import com.google.gson.annotations.SerializedName

internal data class DirectionsResponse(
    @SerializedName("routes") val routes: List<DirectionsRoute>?,
    @SerializedName("status") val status: String,
    @SerializedName("available_travel_modes") val availableTravelModes: List<String>?,
    @SerializedName("error_message") val errorMessage: String?,
    @SerializedName("geocoded_waypoints") val geocodedWaypoints: List<DirectionsGeocodedWaypoint>?
)
