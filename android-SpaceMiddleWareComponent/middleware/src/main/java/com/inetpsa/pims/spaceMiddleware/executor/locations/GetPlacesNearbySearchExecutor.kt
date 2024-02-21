package com.inetpsa.pims.spaceMiddleware.executor.locations

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.model.locations.DirectionParams.LatLng
import com.inetpsa.pims.spaceMiddleware.model.locations.NearbySearchParams
import com.inetpsa.pims.spaceMiddleware.model.locations.NearbySearchResponse
import com.inetpsa.pims.spaceMiddleware.model.locations.PlacesOutput
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class GetPlacesNearbySearchExecutor(command: BaseCommand) :
    BaseLocationExecutor<NearbySearchParams, PlacesOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): NearbySearchParams {
        val latitude: Double = parameters has Constants.PARAM_LAT
        val longitude: Double = parameters has Constants.PARAM_LNG
        val location = LatLng(latitude, longitude)
        val radius: Int = parameters has Constants.PARAM_RADIUS
        val keyword: String = parameters has Constants.PARAM_KEYWORD
        return NearbySearchParams(location, radius, keyword)
    }

    override suspend fun execute(input: NearbySearchParams): NetworkResponse<PlacesOutput> {
        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_LOCATION to input.location.toString(),
            Constants.PARAM_RADIUS to input.radius.toString(),
            Constants.PARAM_KEYWORD to input.keyWord,
            Constants.QUERY_PARAM_KEY_KEY to googleApiKey
        )

        val request = request(
            type = NearbySearchResponse::class.java,
            urls = arrayOf("place/nearbysearch/json"),
            queries = queries
        )

        return communicationManager.get<NearbySearchResponse>(request, TokenType.None).transform { response ->
            when (response.status) {
                Constants.STATUS_OK -> {
                    val places = response.results
                    if (places.isNullOrEmpty()) {
                        NetworkResponse.Failure(PimsErrors.zeroResults())
                    } else {
                        NetworkResponse.Success(PlacesOutput(places))
                    }
                }

                else -> NetworkResponse.Failure(PimsErrors.locationServerError(response.errorMessage))
            }
        }
    }
}
