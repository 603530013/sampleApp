package com.inetpsa.pims.spaceMiddleware.executor.assistance

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.assistance.SendInfoForAssistancePsaParams
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.AssistanceResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull

@Deprecated("try to switch to use this class SetAssistancePSAExecutor")
internal class SendInfoForAssistancePsaExecutor(command: BaseCommand) :
    BasePsaExecutor<SendInfoForAssistancePsaParams, AssistanceResponse>(command) {

    override fun params(parameters: Map<String, Any?>?): SendInfoForAssistancePsaParams =
        SendInfoForAssistancePsaParams(
            vin = parameters has Constants.BODY_PARAM_VIN,
            category = parameters has Constants.BODY_PARAM_CATEGORY,
            latitude = parameters has Constants.BODY_PARAM_LATITUDE,
            longitude = parameters has Constants.BODY_PARAM_LONGITUDE,
            phoneNumber = parameters has Constants.BODY_PARAM_PHONE_NUMBER,
            country = parameters hasOrNull Constants.BODY_PARAM_COUNTRY
        )

    override suspend fun execute(input: SendInfoForAssistancePsaParams): NetworkResponse<AssistanceResponse> {
        val request = request(
            type = AssistanceResponse::class.java,
            urls = arrayOf("/car/v1/rsa/assistance"),
            body = input.toJson()
        )

        return communicationManager.post(request, MiddlewareCommunicationManager.MymToken)
    }
}
