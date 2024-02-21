package com.inetpsa.pims.spaceMiddleware.model.locations

internal data class DirectionParams(val start: LatLng, val end: LatLng) {

    internal data class LatLng(val latitude: Double, val longitude: Double) {

        override fun toString(): String {
            return "$latitude,$longitude"
        }
    }
}
