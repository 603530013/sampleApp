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
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.department.BaseNaftaDepartmentFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.hasLocalDate

internal class GetAgendaForNaftaFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseNaftaDepartmentFcaExecutor<AgendaInput, DealerAgendaNAFTAResponse>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): AgendaInput =
        AgendaInput(
            dealerId = parameters has Appointment.BOOKING_ID,
            startDate = parameters hasLocalDate Appointment.START_DATE,
            timeFence = parameters hasEnum Appointment.TIME_FENCE,
            vin = parameters has Input.VIN
        )

    override suspend fun execute(
        input: AgendaInput,
        token: String,
        departmentId: String
    ): NetworkResponse<DealerAgendaNAFTAResponse> {
        val startDate = input.startDateMilliSeconds.toString()
        val endDate = input.endDateMilliSeconds.toString()

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_START_DATE to startDate,
            Constants.QUERY_PARAM_KEY_END_DATE to endDate
        )

        val request = request(
            object : TypeToken<DealerAgendaNAFTAResponse>() {}.type,
            urls = arrayOf(
                "/v1/servicescheduler/department/",
                departmentId,
                "/timesegments"
            ),
            queries = queries,
            headers = headers(token)
        )

        return communicationManager.get(request, TokenType.AWSToken(FCAApiKey.SDP))
    }
}
