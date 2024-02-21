package com.inetpsa.pims.spaceMiddleware.executor.dealer

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.AdvisorDealerReviewConfiguration
import com.inetpsa.pims.spaceMiddleware.model.dealer.GetAdvisorDealerReviewConfigurationPsaParams
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager

@Deprecated("should be replaced with GetDealerReviewPsaExecutor")
internal class GetAdvisorDealerReviewConfigurationPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<GetAdvisorDealerReviewConfigurationPsaParams, AdvisorDealerReviewConfiguration>(command) {

    override fun params(parameters: Map<String, Any?>?): GetAdvisorDealerReviewConfigurationPsaParams =
        GetAdvisorDealerReviewConfigurationPsaParams(
            parameters has Constants.PARAM_VIN,
            parameters has Constants.BODY_PARAM_VEHICLE_ID_TYPE,
            parameters has Constants.BODY_PARAM_SERVICE_TYPE
        )

    override suspend fun execute(input: GetAdvisorDealerReviewConfigurationPsaParams):
        NetworkResponse<AdvisorDealerReviewConfiguration> {
        val queries = mapOf(
            Constants.BODY_PARAM_VEHICLE_ID_TYPE to input.vehicleIdType,
            Constants.BODY_PARAM_SERVICE_TYPE to input.serviceType
        )

        val request = request(
            AdvisorDealerReviewConfiguration::class.java,
            arrayOf("/shop/v1/reviews/service/settings/", input.vin),
            queries = queries
        )
        return communicationManager.get(request, MiddlewareCommunicationManager.MymToken)
    }
}
