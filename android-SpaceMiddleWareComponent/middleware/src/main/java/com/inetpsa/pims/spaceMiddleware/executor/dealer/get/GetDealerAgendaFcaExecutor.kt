package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Failure
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.Brand.MASERATI
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.mmx.foundation.tools.Market.LATAM
import com.inetpsa.mmx.foundation.tools.Market.NAFTA
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.agenda.GetAgendaForEmeaFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.agenda.GetAgendaForLatamFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.agenda.GetAgendaForMaseratiFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.agenda.GetAgendaForNaftaFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.agenda.AppointmentAgendaEmeaMapper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.agenda.AppointmentAgendaLatamMapper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.agenda.AppointmentAgendaMaseratiMapper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.agenda.AppointmentAgendaNaftaMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.DepartmentIdInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TransportationOptionsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAdvisorResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.unwrap
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

internal class GetDealerAgendaFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<Map<String, Any?>, AgendaOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) = parameters.orEmpty()

    override suspend fun execute(input: Map<String, Any?>): NetworkResponse<AgendaOutput> =
        when {
            configurationManager.brand == MASERATI -> handleMaseratiFlow(input)
            configurationManager.market == EMEA -> handleEmeaFlow(input)
            configurationManager.market == NAFTA -> handleNaftaFlow(input)
            configurationManager.market == LATAM -> handleLatamFlow(input)
            else -> throw PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)
        }

    internal suspend fun handleNaftaFlow(input: Map<String, Any?>): NetworkResponse<AgendaOutput> =
        coroutineScope {
            val bookingId: String = input has Appointment.BOOKING_ID
            BookingOnlineCache.checkIfSameDealer(bookingId)

            val asyncTransportations = async {
                val departmentInput = DepartmentIdInput(dealerId = bookingId)
                GetTransportationOptionsForNaftaFCAExecutor(middlewareComponent, params).execute(departmentInput)
            }
            val asyncAdvisors = async {
                val tokenInput = OssTokenInput(dealerId = bookingId)
                GetAdvisorForNaftaFcaExecutor(middlewareComponent, params).execute(tokenInput)
            }
            val asyncAgenda = async {
                GetAgendaForNaftaFcaExecutor(middlewareComponent, input).execute()
            }
            val responses = awaitAll(asyncTransportations, asyncAdvisors, asyncAgenda)

            @Suppress("UNCHECKED_CAST")
            val transportationsResponse = responses[0] as NetworkResponse<TransportationOptionsResponse>

            @Suppress("UNCHECKED_CAST")
            val advisorsResponse = responses[1] as NetworkResponse<DealerAdvisorResponse>

            @Suppress("UNCHECKED_CAST")
            val agendaResponse = responses[2] as NetworkResponse<DealerAgendaNAFTAResponse>

            if (transportationsResponse is Failure ||
                advisorsResponse is Failure ||
                agendaResponse is Failure
            ) {
                transportationsResponse as? Failure ?: advisorsResponse as? Failure ?: agendaResponse as Failure
            } else {
                val transportations = transportationsResponse.unwrap()
                val advisors = advisorsResponse.unwrap()
                val agenda = agendaResponse.unwrap()
                Success(AppointmentAgendaNaftaMapper().transformOutput(transportations, advisors, agenda))
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleLatamFlow(input: Map<String, Any?>): NetworkResponse<AgendaOutput> =
        GetAgendaForLatamFcaExecutor(middlewareComponent, input)
            .execute()
            .map { AppointmentAgendaLatamMapper().transformOutput(it) }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleEmeaFlow(input: Map<String, Any?>): NetworkResponse<AgendaOutput> =
        GetAgendaForEmeaFcaExecutor(middlewareComponent, input)
            .execute()
            .map { AppointmentAgendaEmeaMapper().transformOutput(it) }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleMaseratiFlow(input: Map<String, Any?>): NetworkResponse<AgendaOutput> =
        GetAgendaForMaseratiFcaExecutor(middlewareComponent, input)
            .execute()
            .map { AppointmentAgendaMaseratiMapper().transformOutput(it) }
}
