package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history.AppointmentHistoryLatamMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryLATAMResponse
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetHistoryForLatamFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<String, HistoryOutput>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Input.VIN

    override suspend fun execute(input: String): NetworkResponse<HistoryOutput> {
        val request = request(
            type = AppointmentHistoryLATAMResponse::class.java,
            urls = arrayOf("/v1/accounts/$uid/vehicles/$input/servicescheduler/appointments")
        )
        return communicationManager
            .get<AppointmentHistoryLATAMResponse>(request, TokenType.AWSToken(SDP))
            .map { response ->
                BookingOnlineCache.write(response)
                AppointmentHistoryLatamMapper().transformOutput(response)
            }
    }
}
