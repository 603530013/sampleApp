package com.inetpsa.pims.spaceMiddleware.executor.radioplayer

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.recommendations.GetRecommendationListPsaRpParams
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.stations.StationListPsaRp
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.filterNotNull
import com.inetpsa.pims.spaceMiddleware.util.has
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull

@Deprecated("should be replaced with GetRadioPlayerRecommendationsPsaExecutor")
internal class GetRecommendationsPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<GetRecommendationListPsaRpParams, StationListPsaRp>(command) {

    override fun params(parameters: Map<String, Any?>?): GetRecommendationListPsaRpParams {
        val factors: String = parameters has Constants.PARAM_FACTORS
        val isGEO = factors.split(",").contains("GEO")
        return GetRecommendationListPsaRpParams(
            country = parameters has Constants.PARAM_COUNTRY,
            rpuid = parameters hasOrNull Constants.PARAM_RPUID,
            factors = factors,
            latitude = parameters.has(Constants.PARAM_LAT, !isGEO),
            longitude = parameters.has(Constants.PARAM_LNG, !isGEO)
        )
    }

    override suspend fun execute(input: GetRecommendationListPsaRpParams): NetworkResponse<StationListPsaRp> {
        val queries = mapOf(
            Constants.PARAM_COUNTRY to input.country,
            Constants.PARAM_FACTORS to input.factors,
            Constants.PARAM_RPUID to input.rpuid,
            Constants.PARAM_LAT to input.latitude?.toString(),
            Constants.PARAM_LNG to input.longitude?.toString()
        ).filterNotNull()

        val request = request(
            StationListPsaRp::class.java,
            arrayOf("/shop/v1/recommendations"),
            queries = queries
        )

        return communicationManager.get(request, MiddlewareCommunicationManager.MymToken)
    }
}
