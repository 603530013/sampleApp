package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history.AppointmentHistoryMaseratiMapper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsMaseratiResponse
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetAppointmentDetailsForMaseratiExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<DetailsInput, DetailsOutput>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): DetailsInput = DetailsInput(
        vin = parameters has Constants.PARAM_VIN,
        id = parameters has Constants.PARAM_ID
    )

    override suspend fun execute(input: DetailsInput): NetworkResponse<DetailsOutput> {
        val request = request(
            type = AppointmentDetailsMaseratiResponse::class.java,
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
            .get<AppointmentDetailsMaseratiResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .map { AppointmentHistoryMaseratiMapper().transformOutput(it) }
    }
}
