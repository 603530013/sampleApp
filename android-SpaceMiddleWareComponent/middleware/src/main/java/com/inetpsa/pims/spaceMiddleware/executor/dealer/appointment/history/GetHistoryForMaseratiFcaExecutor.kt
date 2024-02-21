package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history.AppointmentHistoryMaseratiMapper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryMaseratiResponse
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetHistoryForMaseratiFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<String, HistoryOutput>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Input.VIN

    override suspend fun execute(input: String): NetworkResponse<HistoryOutput> {
        val request = request(
            type = AppointmentHistoryMaseratiResponse::class.java,
            urls = arrayOf("/v1/accounts/$uid/vehicles/$input/servicescheduler/appointment/history")
        )
        return communicationManager
            .get<AppointmentHistoryMaseratiResponse>(request, TokenType.AWSToken(SDP))
            .map { AppointmentHistoryMaseratiMapper().transformOutput(it) }
    }
}
