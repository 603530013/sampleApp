package com.inetpsa.pims.spaceMiddleware.model.locations

import com.inetpsa.pims.spaceMiddleware.model.locations.DirectionParams.LatLng

internal data class NearbySearchParams(val location: LatLng, val radius: Int, val keyWord: String)
