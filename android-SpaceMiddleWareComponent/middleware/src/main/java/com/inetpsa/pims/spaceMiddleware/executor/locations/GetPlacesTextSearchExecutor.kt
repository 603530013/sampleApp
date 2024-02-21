package com.inetpsa.pims.spaceMiddleware.executor.locations

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.model.locations.PlacesOutput
import com.inetpsa.pims.spaceMiddleware.model.locations.TextSearchResponse
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class GetPlacesTextSearchExecutor(command: BaseCommand) : BaseLocationExecutor<String, PlacesOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_KEYWORD

    override suspend fun execute(input: String): NetworkResponse<PlacesOutput> {
        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_QUERY to input,
            Constants.QUERY_PARAM_KEY_KEY to googleApiKey
        )

        val request = request(
            type = TextSearchResponse::class.java,
            urls = arrayOf("place/textsearch/json"),
            queries = queries
        )

        return communicationManager.get<TextSearchResponse>(request, TokenType.None).transform { response ->
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
