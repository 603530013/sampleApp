package com.inetpsa.pims.spaceMiddleware.command.locations

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseExecutor
import com.inetpsa.pims.spaceMiddleware.executor.locations.GetDirectionsExecutor
import com.inetpsa.pims.spaceMiddleware.executor.locations.GetPlaceDetailsExecutor
import com.inetpsa.pims.spaceMiddleware.executor.locations.GetPlacesNearbySearchExecutor
import com.inetpsa.pims.spaceMiddleware.executor.locations.GetPlacesTextSearchExecutor

internal class GetLocationsCommand : BaseCommand() {

    override suspend fun getExecutor(): BaseExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_TEXT_SEARCH -> GetPlacesTextSearchExecutor(this)
        Constants.PARAM_ACTION_NEARBY_SEARCH -> GetPlacesNearbySearchExecutor(this)
        Constants.PARAM_ACTION_PLACE_DETAILS -> GetPlaceDetailsExecutor(this)
        Constants.PARAM_ACTION_DIRECTIONS_ROUTE -> GetDirectionsExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }
}
