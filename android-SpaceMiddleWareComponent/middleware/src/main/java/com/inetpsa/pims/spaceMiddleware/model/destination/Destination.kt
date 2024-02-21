package com.inetpsa.pims.spaceMiddleware.model.destination

internal data class Destination(
    val location: Location,
    val routePreference: String
) {

    internal data class Location(
        val placeId: String? = null,
        val latitude: Double? = null,
        val longitude: Double? = null,
        val address: Address? = null,
        val name: String? = null,
        val description: String? = null,
        val url: String? = null,
        val phoneNumber: String? = null
    ) {

        internal data class Address(
            val streetName: String? = null,
            val houseNumber: String? = null,
            val postalNumber: String? = null,
            val cityName: String? = null,
            val countryName: String? = null,
            val countryCode: String? = null,
            val provinceName: String? = null,
            val provinceCode: String? = null
        )
    }
}
