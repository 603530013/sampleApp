package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token.BaseNaftaTokenDealerFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history.AppointmentHistoryNaftaMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryNaftaInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryNAFTAResponse
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetHistoryForNaftaFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseNaftaTokenDealerFcaExecutor<HistoryNaftaInput, HistoryOutput>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): HistoryNaftaInput =
        HistoryNaftaInput(
            dealerId = parameters has Appointment.BOOKING_ID,
            vin = parameters has Input.VIN
        )

    override suspend fun execute(input: HistoryNaftaInput, token: String): NetworkResponse<HistoryOutput> {
        val request = request(
            type = AppointmentHistoryNAFTAResponse::class.java,
            urls = arrayOf("/v1/accounts/$uid/vehicles/${input.vin}/servicescheduler/appointments"),
            headers = headers(token)
        )
        return communicationManager
            .get<AppointmentHistoryNAFTAResponse>(request, TokenType.AWSToken(SDP))
            .map { response ->
                BookingOnlineCache.write(response)
                AppointmentHistoryNaftaMapper().transformOutput(response)
            }
    }
}
