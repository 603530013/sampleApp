package com.inetpsa.pims.spaceMiddleware.executor.locations

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.model.locations.Place
import com.inetpsa.pims.spaceMiddleware.model.locations.PlaceDetailsResponse
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class GetPlaceDetailsExecutor(command: BaseCommand) : BaseLocationExecutor<String, Place>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_PLACE_ID

    override suspend fun execute(input: String): NetworkResponse<Place> {
        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_PLACE_ID to input,
            Constants.QUERY_PARAM_KEY_KEY to googleApiKey
        )

        val request = request(
            type = PlaceDetailsResponse::class.java,
            urls = arrayOf("place/details/json"),
            queries = queries
        )

        return communicationManager.get<PlaceDetailsResponse>(request, TokenType.None).transform { response ->
            when (response.status) {
                Constants.STATUS_OK -> {
                    val place = response.result
                    if (place == null) {
                        NetworkResponse.Failure(PimsErrors.zeroResults())
                    } else {
                        NetworkResponse.Success(place)
                    }
                }

                else -> NetworkResponse.Failure(PimsErrors.locationServerError(response.errorMessage))
            }
        }
    }
}
