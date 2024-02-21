package com.inetpsa.pims.spaceMiddleware.executor.locations

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.model.locations.DirectionOutput
import com.inetpsa.pims.spaceMiddleware.model.locations.DirectionParams
import com.inetpsa.pims.spaceMiddleware.model.locations.DirectionParams.LatLng
import com.inetpsa.pims.spaceMiddleware.model.locations.DirectionsResponse
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class GetDirectionsExecutor(command: BaseCommand) :
    BaseLocationExecutor<DirectionParams, DirectionOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): DirectionParams {
        val originLatitude: Double = parameters has Constants.PARAM_ORIGIN_LATITUDE
        val originLongitude: Double = parameters has Constants.PARAM_ORIGIN_LONGITUDE
        val start = LatLng(originLatitude, originLongitude)

        val directionLatitude: Double = parameters has Constants.PARAM_DIRECTION_LATITUDE
        val directionLongitude: Double = parameters has Constants.PARAM_DIRECTION_LONGITUDE
        val end = LatLng(directionLatitude, directionLongitude)

        return DirectionParams(start, end)
    }

    override suspend fun execute(input: DirectionParams): NetworkResponse<DirectionOutput> {
        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_ORIGIN to input.start.toString(),
            Constants.QUERY_PARAM_KEY_DESTINATION to input.end.toString(),
            Constants.QUERY_PARAM_KEY_KEY to googleApiKey
        )

        val request = request(
            type = DirectionsResponse::class.java,
            urls = arrayOf("directions/json"),
            queries = queries
        )

        return communicationManager.get<DirectionsResponse>(request, TokenType.None).transform { response ->
            when (response.status) {
                Constants.STATUS_OK -> {
                    val directions = response.routes
                    if (directions.isNullOrEmpty()) {
                        NetworkResponse.Failure(PimsErrors.zeroResults())
                    } else {
                        NetworkResponse.Success(DirectionOutput(directions))
                    }
                }

                else -> NetworkResponse.Failure(PimsErrors.locationServerError(response.errorMessage))
            }
        }
    }
}
