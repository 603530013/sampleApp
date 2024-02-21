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
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.hasLocalDate
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.ifSuccess
import java.time.ZoneId

internal class GetAgendaForLatamFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<AgendaInput, DealerAgendaLATAMResponse>(middlewareComponent, params) {

    internal companion object {

        const val MINIMUM_HOUR_CONFIG: Long = 3
    }

    override fun params(parameters: Map<String, Any?>?): AgendaInput =
        AgendaInput(
            dealerId = parameters has Appointment.BOOKING_ID,
            startDate = parameters hasLocalDate Appointment.START_DATE,
            timeFence = parameters hasEnum Appointment.TIME_FENCE,
            vin = parameters has Input.VIN,
            serviceIds = parameters hasOrNull Appointment.SERVICES
        )

    override suspend fun execute(input: AgendaInput): NetworkResponse<DealerAgendaLATAMResponse> {
        val startDate = input.startDate.atStartOfDay(ZoneId.of("UTC")).plusHours(MINIMUM_HOUR_CONFIG)
            .toInstant()
            .toEpochMilli()

        val endDate = input.endDate.atStartOfDay(ZoneId.of("UTC")).plusHours(MINIMUM_HOUR_CONFIG)
            .toInstant()
            .toEpochMilli()

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_LATAM_START_DATE to startDate.toString(),
            Constants.QUERY_PARAM_KEY_LATAM_END_DATE to endDate.toString(),
            Constants.QUERY_PARAM_KEY_SERVICES_IDS to input.serviceIds.orEmpty(),
            Constants.QUERY_PARAM_KEY_DEALER_ID to input.dealerId
        )

        val request = request(
            type = object : TypeToken<DealerAgendaLATAMResponse>() {}.type,
            urls = arrayOf("/v1/servicescheduler/timesegments"),
            queries = queries
        )
        return communicationManager
            .get<DealerAgendaLATAMResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .ifSuccess { BookingOnlineCache.write(it) }
    }
}
