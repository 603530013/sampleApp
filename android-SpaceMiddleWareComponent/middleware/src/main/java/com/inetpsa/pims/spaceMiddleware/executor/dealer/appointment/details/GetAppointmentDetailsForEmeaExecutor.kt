package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history.AppointmentHistoryEmeaMapper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsEmeaResponse
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetAppointmentDetailsForEmeaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>?
) : BaseFcaExecutor<DetailsInput, DetailsOutput>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?) = DetailsInput(
        vin = parameters has Constants.PARAM_VIN,
        id = parameters has Constants.PARAM_ID
    )

    override suspend fun execute(input: DetailsInput): NetworkResponse<DetailsOutput> {
        val request = request(
            type = AppointmentDetailsEmeaResponse::class.java,
            urls = arrayOf(
                "/v1/accounts/",
                uid,
                "/vehicles/",
                input.vin,
                "/servicescheduler/appointment/",
                input.id
            )
        )
        return communicationManager
            .get<AppointmentDetailsEmeaResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .map { AppointmentHistoryEmeaMapper().transformOutput(it) }
    }
}
