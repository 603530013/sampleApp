package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.psa.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AgendaResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.hasLocalDate
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.map
import java.time.format.DateTimeFormatterBuilder

internal class GetDealerAgendaPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<AgendaInput, AgendaOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): AgendaInput {
        return AgendaInput(
            dealerId = parameters has Appointment.BOOKING_ID,
            startDate = parameters hasLocalDate Appointment.START_DATE,
            timeFence = parameters hasEnum Appointment.TIME_FENCE,
            vin = parameters has Input.VIN,
            serviceIds = parameters hasOrNull Appointment.SERVICES
        )
    }

    override suspend fun execute(input: AgendaInput): NetworkResponse<AgendaOutput> {
        val formatter = DateTimeFormatterBuilder()
            .appendPattern("yyyyMMdd")
            .toFormatter()
        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_FROM to input.startDate.format(formatter),
            Constants.QUERY_PARAM_KEY_TO to input.endDate.format(formatter)
        )

        val request = request(
            type = AgendaResponse::class.java,
            urls = arrayOf("/shop/v1/rdv/agenda/${input.dealerId}"),
            queries = queries
        )

        return communicationManager
            .get<AgendaResponse>(request, MiddlewareCommunicationManager.MymToken)
            .map { response ->
                BookingOnlineCache.psaAgenda = response
                transformToAgendaOutput(response)
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToAgendaOutput(response: AgendaResponse): AgendaOutput =
        AgendaOutput(
            rrdi = response.rrdi,
            period = response.period,
            from = response.from,
            to = response.to,
            type = response.type,
            days = response.days?.map { transformToDayItemOutput(it) }
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToDayItemOutput(response: AgendaResponse.DaysItem): AgendaOutput.DaysItem =
        AgendaOutput.DaysItem(
            date = response.date,
            slots = response.slots?.map {
                AgendaOutput.DaysItem.SlotsItem(
                    receptionAvailable = it.receptionAvailable,
                    start = it.start,
                    discount = it.discount,
                    end = it.end,
                    receptionTotal = it.receptionTotal
                )
            }
        )
}
