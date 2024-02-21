package com.inetpsa.pims.spaceMiddleware.helper

import com.inetpsa.pims.spaceMiddleware.model.locations.Place
import com.inetpsa.pims.spaceMiddleware.model.locations.Place.Geometry
import com.inetpsa.pims.spaceMiddleware.model.locations.Place.Geometry.Location
import com.inetpsa.pims.spaceMiddleware.model.locations.Place.Geometry.Viewport

/**
 * Shared fun to create a Place object with ease.
 * If you want change some properties, please don't do here directly but use .copy() method.
 */
internal fun emptyPlaceModel(): Place = Place(
    addressComponents = listOf(),
    formattedAddress = "",
    formattedPhoneNumber = null,
    geometry = Geometry(
        location = Location(
            lat = 0.0,
            lng = 0.0
        ),
        viewport = Viewport(
            northeast = Location(
                lat = 0.0,
                lng = 0.0
            ),
            southwest = Location(
                lat = 0.0,
                lng = 0.0
            )
        )
    ),
    icon = "",
    name = "",
    placeId = "",
    rating = 0f,
    userRatingsTotal = 0,
    openingHours = null,
    vicinity = "",
    types = null
)
