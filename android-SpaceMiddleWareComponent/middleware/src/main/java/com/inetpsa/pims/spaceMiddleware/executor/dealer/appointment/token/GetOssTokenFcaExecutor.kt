package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.ServiceSchedulerToken

internal class GetOssTokenFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<OssTokenInput, ServiceSchedulerToken>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): OssTokenInput =
        OssTokenInput(dealerId = parameters has Appointment.BOOKING_ID)

    override suspend fun execute(input: OssTokenInput): NetworkResponse<ServiceSchedulerToken> {
        val queries = mapOf(Constants.QUERY_PARAM_KEY_HINT_DEALER to input.dealerId)

        val request = request(
            object : TypeToken<ServiceSchedulerToken>() {}.type,
            arrayOf("/v1/servicescheduler/token"),
            queries = queries
        )

        return communicationManager.get(request, TokenType.AWSToken(FCAApiKey.SDP))
    }
}
