package com.inetpsa.pims.spaceMiddleware.executor.dealer

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.AdvisorDealerReviewConfiguration
import com.inetpsa.pims.spaceMiddleware.model.dealer.SendAdvisorDealerReviewPsaParams
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager

internal class SendAdvisorDealerReviewPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<SendAdvisorDealerReviewPsaParams, AdvisorDealerReviewConfiguration>(command) {

    override fun params(parameters: Map<String, Any?>?): SendAdvisorDealerReviewPsaParams =
        SendAdvisorDealerReviewPsaParams(
            parameters has Constants.PARAM_SITE_GEO,
            parameters has Constants.BODY_PARAM_VIN,
            parameters has Constants.BODY_PARAM_RATING,
            parameters has Constants.BODY_PARAM_COMMENT,
            parameters has Constants.BODY_PARAM_SERVICE_TYPE,
            parameters has Constants.BODY_PARAM_SEND_CONFIRM_EMAIL,
            parameters has Constants.BODY_PARAM_SERVICE_DATE
        )

    override suspend fun execute(input: SendAdvisorDealerReviewPsaParams):
        NetworkResponse<AdvisorDealerReviewConfiguration> {
        val queries = mapOf(
            Constants.BODY_PARAM_SERVICE_TYPE to input.serviceType
        )

        val body = mapOf(
            Constants.BODY_PARAM_COMMENT to input.comment,
            Constants.BODY_PARAM_RATING to input.rating,
            Constants.BODY_PARAM_SEND_CONFIRM_EMAIL to input.sendConfirmEmail,
            Constants.PARAM_SITE_GEO to input.siteGeo,
            Constants.BODY_PARAM_SERVICE_DATE to input.serviceDate
        )

        val request = request(
            AdvisorDealerReviewConfiguration::class.java,
            arrayOf("/shop/v1/reviews/service/perform/", input.vin),
            body = body.toJson(),
            queries = queries
        )

        return communicationManager.post(request, MiddlewareCommunicationManager.MymToken)
    }
}
