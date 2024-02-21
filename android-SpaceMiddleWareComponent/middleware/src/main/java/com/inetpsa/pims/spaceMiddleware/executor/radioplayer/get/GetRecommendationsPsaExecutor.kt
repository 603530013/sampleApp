package com.inetpsa.pims.spaceMiddleware.executor.radioplayer.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.RecommendationsInput
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.StationsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.RadioPlayerStationsResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.filterNotNull
import com.inetpsa.pims.spaceMiddleware.util.has
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetRecommendationsPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<RecommendationsInput, StationsOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): RecommendationsInput {
        val factors: String = parameters has Constants.PARAM_FACTORS
        val isGEO = factors.split(",").contains("GEO")

        return RecommendationsInput(
            country = parameters has Constants.PARAM_COUNTRY,
            id = parameters hasOrNull Constants.PARAM_ID,
            factors = factors,
            latitude = parameters.has(Constants.PARAM_LAT, !isGEO),
            longitude = parameters.has(Constants.PARAM_LNG, !isGEO)
        )
    }

    override suspend fun execute(input: RecommendationsInput): NetworkResponse<StationsOutput> {
        val queries = mapOf(
            Constants.PARAM_COUNTRY to input.country,
            Constants.PARAM_FACTORS to input.factors,
            Constants.PARAM_RPUID to input.id,
            Constants.PARAM_LAT to input.latitude?.toString(),
            Constants.PARAM_LNG to input.longitude?.toString()
        ).filterNotNull()

        val request = request(
            RadioPlayerStationsResponse::class.java,
            arrayOf("/shop/v1/recommendations"),
            queries = queries
        )

        return communicationManager.get<RadioPlayerStationsResponse>(request, MiddlewareCommunicationManager.MymToken)
            .map { transformToStations(it) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToStation(response: RadioPlayerStationsResponse.StationsItem): StationsOutput.Station =
        StationsOutput.Station(
            id = response.rpuid,
            name = response.name,
            description = response.description,
            country = response.country,
            relevanceIndex = response.relevanceIndex,
            multimedia = response.multimedia
                ?.mapNotNull { item ->
                    item?.let { StationsOutput.Station.Multimedia(url = it.url, width = it.width, height = it.height) }
                }?.sortedBy { it.width },
            liveStreams = response.liveStreams
                ?.mapNotNull { item ->
                    item?.streamSource?.url
                        .isNullOrBlank()
                        .takeIf { !it }
                        ?.let {
                            StationsOutput.Station.LiveStream(
                                mime = item?.streamSource?.mimeValue,
                                url = item?.streamSource?.url,
                                bitRate = item?.bitRate?.target
                            )
                        }
                }?.sortedByDescending { it.bitRate }
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToStations(response: RadioPlayerStationsResponse): StationsOutput {
        val items = response.stations
            ?.map { transformToStation(it) }
            .orEmpty()
        return StationsOutput(items)
    }
}
