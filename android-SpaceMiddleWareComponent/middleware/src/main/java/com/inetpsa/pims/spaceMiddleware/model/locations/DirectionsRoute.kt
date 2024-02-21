package com.inetpsa.pims.spaceMiddleware.model.locations

import com.google.gson.annotations.SerializedName

internal data class DirectionsRoute(
    @SerializedName("bounds") val bounds: Bounds,
    @SerializedName("copyrights") val copyrights: String,
    @SerializedName("legs") val legs: List<DirectionsLeg>,
    @SerializedName("overview_polyline") val overviewPolyline: DirectionsPolyline,
    @SerializedName("summary") val summary: String,
    @SerializedName("warnings") val warnings: List<String>,
    @SerializedName("waypoint_order") val waypointOrder: List<String>,
    @SerializedName("fare") val fare: Any?
) {

    internal data class Bounds(
        @SerializedName("northeast") val northeast: LatLngLiteral,
        @SerializedName("southwest") val southwest: LatLngLiteral
    )

    internal data class DirectionsLeg(
        @SerializedName("end_address") val endAddress: String,
        @SerializedName("end_location") val endLocation: LatLngLiteral,
        @SerializedName("start_address") val startAddress: String,
        @SerializedName("start_location") val startLocation: LatLngLiteral,
        @SerializedName("steps") val steps: List<DirectionsStep>,
        @SerializedName("traffic_speed_entry") val trafficSpeedEntry: List<DirectionsTrafficSpeedEntry>,
        @SerializedName("via_waypoint") val viaWaypoint: List<DirectionsViaWaypoint>,
        @SerializedName("arrival_time") val arrivalTime: TimeZoneTextValueObject?,
        @SerializedName("departure_time") val departureTime: TimeZoneTextValueObject?,
        @SerializedName("distance") val distance: TextValueObject,
        @SerializedName("duration") val duration: TextValueObject,
        @SerializedName("duration_in_traffic") val durationInTraffic: TextValueObject?
    )

    internal data class DirectionsTrafficSpeedEntry(
        @SerializedName("offset_meters") val offsetMeters: Int,
        @SerializedName("speed_category") val speedCategory: String
    )

    internal data class DirectionsViaWaypoint(
        @SerializedName("location") val location: LatLngLiteral,
        @SerializedName("step_index") val stepIndex: Int,
        @SerializedName("step_interpolation") val stepInterpolation: Int
    )

    internal data class DirectionsStep(
        @SerializedName("duration") val duration: TextValueObject,
        @SerializedName("end_location") val endLocation: LatLngLiteral,
        @SerializedName("html_instructions") val htmlInstructions: String,
        @SerializedName("polyline") val polyline: DirectionsPolyline,
        @SerializedName("start_location") val startLocation: LatLngLiteral,
        @SerializedName("travel_mode") val travelMode: String,
        @SerializedName("distance") val distance: TextValueObject,
        @SerializedName("maneuver") val maneuver: String?,
        @SerializedName("transit_details") val transitDetails: DirectionsTransitDetails?
    )

    internal data class DirectionsTransitDetails(
        @SerializedName("arrival_sto") val arrivalSto: DirectionsTransitStop,
        @SerializedName("arrival_time") val arrivalTime: TimeZoneTextValueObject,
        @SerializedName("departure_stop") val departureStop: TimeZoneTextValueObject,
        @SerializedName("departure_time") val departureTime: TimeZoneTextValueObject,
        @SerializedName("headsign") val headsign: String,
        @SerializedName("headway") val headway: Int,
        @SerializedName("line") val line: DirectionsTransitLine,
        @SerializedName("num_stops") val numStops: Int,
        @SerializedName("trip_short_name") val tripShortName: String
    )

    internal data class DirectionsTransitStop(
        @SerializedName("location") val location: LatLngLiteral,
        @SerializedName("name") val name: String
    )

    internal data class TimeZoneTextValueObject(
        @SerializedName("text") val text: String,
        @SerializedName("time_zone") val timeZone: String,
        @SerializedName("value") val value: Long
    )

    internal data class DirectionsTransitLine(
        @SerializedName("agencies") val agencies: DirectionsTransitAgency,
        @SerializedName("name") val name: String,
        @SerializedName("color") val color: String,
        @SerializedName("icon") val icon: String,
        @SerializedName("short_name") val shortName: String,
        @SerializedName("text_color") val textColor: String,
        @SerializedName("url") val url: String,
        @SerializedName("vehicle") val vehicle: DirectionsTransitVehicle
    )

    internal data class DirectionsTransitAgency(
        @SerializedName("name") val name: String,
        @SerializedName("phone") val phone: String,
        @SerializedName("url") val url: String
    )

    internal data class DirectionsTransitVehicle(
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String,
        @SerializedName("icon") val icon: String,
        @SerializedName("local_icon") val localIcon: String
    )

    internal data class LatLngLiteral(
        @SerializedName("lat") val lat: Double,
        @SerializedName("lng") val lng: Double
    )

    internal data class DirectionsPolyline(
        @SerializedName("points") val points: String
    )

    internal data class TextValueObject(
        @SerializedName("text") val text: String,
        @SerializedName("value") val value: Int
    )
}
