package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.agenda

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaMaseratiResponse
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.hasLocalDate
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull

internal class GetAgendaForMaseratiFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<AgendaInput, DealerAgendaMaseratiResponse>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): AgendaInput =
        AgendaInput(
            dealerId = parameters has Appointment.BOOKING_ID,
            dealerLocation = parameters hasOrNull Appointment.BOOKING_LOCATION,
            startDate = parameters hasLocalDate Appointment.START_DATE,
            timeFence = parameters hasEnum Appointment.TIME_FENCE,
            vin = parameters has Input.VIN
        )

    override suspend fun execute(input: AgendaInput): NetworkResponse<DealerAgendaMaseratiResponse> {
        var timeFence = input.timeFence
        if (input.timeFence == AgendaInput.TimeFence.WEEK) {
            timeFence = AgendaInput.TimeFence.MONTH
        }
        val queries = mutableMapOf(
            Constants.QUERY_PARAM_KEY_DEALER_ID to input.dealerId,
            Constants.QUERY_PARAM_KEY_DATE_STRING to input.startDate.toString(),
            Constants.QUERY_PARAM_TIME_FENCE to timeFence.name.lowercase()
        )
        input.dealerLocation?.let { queries[Constants.QUERY_PARAM_KEY_LOCATION] = it }

        val request = request(
            type = object : TypeToken<DealerAgendaMaseratiResponse>() {}.type,
            urls = arrayOf("/v1/accounts/", uid, "/vehicles/", input.vin, "/servicescheduler/agenda"),
            queries = queries
        )

        return communicationManager.get(request, TokenType.AWSToken(FCAApiKey.SDP))
    }
}
