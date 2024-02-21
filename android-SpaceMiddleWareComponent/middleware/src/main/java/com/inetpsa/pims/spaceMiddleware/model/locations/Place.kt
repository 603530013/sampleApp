package com.inetpsa.pims.spaceMiddleware.model.locations

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.locations.Place.AddressComponent.AddressComponentType

internal data class Place(
    @SerializedName("address_components") val addressComponents: List<AddressComponent>?,
    @SerializedName("formatted_address") val formattedAddress: String?,
    @SerializedName("formatted_phone_number") val formattedPhoneNumber: String?,
    @SerializedName("geometry") val geometry: Geometry,
    @SerializedName("icon") val icon: String,
    @SerializedName("name") val name: String,
    @SerializedName("place_id") val placeId: String,
    @SerializedName("rating") val rating: Float,
    @SerializedName("vicinity") val vicinity: String?,
    @SerializedName("user_ratings_total") val userRatingsTotal: Int,
    @SerializedName("opening_hours") val openingHours: OpeningHours?,
    @SerializedName("types") val types: List<AddressComponentType>?
) {

    internal data class AddressComponent(
        @SerializedName("long_name") val longName: String,
        @SerializedName("short_name") val shortName: String,
        @SerializedName("types") val types: List<AddressComponentType>
    ) {

        @Keep
        internal enum class AddressComponentType(val value: String) {

            @SerializedName("street_address")
            STREET_ADDRESS("streetAddress"),

            @SerializedName("route")
            ROUTE("route"),

            @SerializedName("intersection")
            INTERSECTION("intersection"),

            @SerializedName("political")
            POLITICAL("political"),

            @SerializedName("country")
            COUNTRY("country"),

            @SerializedName("administrative_area_level_1")
            ADMINISTRATIVE_AREA_LEVEL_1("administrativeAreaLevel1"),

            @SerializedName("administrative_area_level_2")
            ADMINISTRATIVE_AREA_LEVEL_2("administrativeAreaLevel2"),

            @SerializedName("administrative_area_level_3")
            ADMINISTRATIVE_AREA_LEVEL_3("administrativeAreaLevel3"),

            @SerializedName("administrative_area_level_4")
            ADMINISTRATIVE_AREA_LEVEL_4("administrativeAreaLevel4"),

            @SerializedName("administrative_area_level_5")
            ADMINISTRATIVE_AREA_LEVEL_5("administrativeAreaLevel5"),

            @SerializedName("colloquial_area")
            COLLOQUIAL_AREA("colloquialArea"),

            @SerializedName("locality")
            LOCALITY("locality"),

            @SerializedName("ward")
            WARD("ward"),

            @SerializedName("sublocality")
            SUBLOCALITY("sublocality"),

            @SerializedName("sublocality_level_1")
            SUBLOCALITY_LEVEL_1("sublocalityLevel1"),

            @SerializedName("sublocality_level_2")
            SUBLOCALITY_LEVEL_2("sublocalityLevel2"),

            @SerializedName("sublocality_level_3")
            SUBLOCALITY_LEVEL_3("sublocalityLevel3"),

            @SerializedName("sublocality_level_4")
            SUBLOCALITY_LEVEL_4("sublocalityLevel4"),

            @SerializedName("sublocality_level_5")
            SUBLOCALITY_LEVEL_5("sublocalityLevel5"),

            @SerializedName("neighborhood")
            NEIGHBORHOOD("neighborhood"),

            @SerializedName("premise")
            PREMISE("premise"),

            @SerializedName("subpremise")
            SUBPREMISE("subpremise"),

            @SerializedName("postal_code")
            POSTAL_CODE("postalCode"),

            @SerializedName("postal_code_prefix")
            POSTAL_CODE_PREFIX("postalCodePrefix"),

            @SerializedName("postal_code_suffix")
            POSTAL_CODE_SUFFIX("postalCodeSuffix"),

            @SerializedName("natural_feature")
            NATURAL_FEATURE("naturalFeature"),

            @SerializedName("airport")
            AIRPORT("airport"),

            @SerializedName("park")
            PARK("park"),

            @SerializedName("point_of_interest")
            POINT_OF_INTEREST("pointOfInterest"),

            @SerializedName("floor")
            FLOOR("floor"),

            @SerializedName("establishment")
            ESTABLISHMENT("establishment"),

            @SerializedName("parking")
            PARKING("parking"),

            @SerializedName("post_box")
            POST_BOX("postBox"),

            @SerializedName("postal_town")
            POSTAL_TOWN("postalTown"),

            @SerializedName("room")
            ROOM("room"),

            @SerializedName("street_number")
            STREET_NUMBER("streetNumber"),

            @SerializedName("bus_station")
            BUS_STATION("busStation"),

            @SerializedName("train_station")
            TRAIN_STATION("trainStation"),

            @SerializedName("subway_station")
            SUBWAY_STATION("subwayStation"),

            @SerializedName("transit_station")
            TRANSIT_STATION("transitStation"),

            @SerializedName("light_rail_station")
            LIGHT_RAIL_STATION("lightRailStation"),

            @SerializedName("gas_station")
            GAS_STATION("gasStation"),

            @SerializedName("unknown")
            UNKNOWN("unknown")
        }
    }

    internal data class Geometry(
        @SerializedName("location") val location: Location,
        @SerializedName("viewport") val viewport: Viewport
    ) {

        internal data class Viewport(
            @SerializedName("northeast") val northeast: Location,
            @SerializedName("southwest") val southwest: Location
        )

        internal data class Location(
            @SerializedName("lat") val lat: Double,
            @SerializedName("lng") val lng: Double
        )
    }

    internal data class OpeningHours(
        @SerializedName("open_now") val openNow: Boolean,
        @SerializedName("periods") val periods: List<OpeningHoursPeriod>?
    ) {

        internal data class OpeningHoursPeriod(
            @SerializedName("open") val open: Open,
            @SerializedName("close") val close: Close
        ) {

            internal data class Open(
                @SerializedName("day") val day: Int,
                @SerializedName("time") val time: String
            )

            internal data class Close(
                @SerializedName("day") val day: Int,
                @SerializedName("time") val time: String
            )
        }
    }
}
