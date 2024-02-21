package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.agenda

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput.DaysItem.SlotsItem
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TransportationOptionsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TransportationOptionsResponse.TransportationOption
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAdvisorResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAdvisorResponse.ServiceAdvisors
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse.Segment
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse.Segment.Slot.ServiceAdvisorsItem
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse.Segment.Slot.TransportationOptionsItem
import java.time.DateTimeException
import java.time.Instant
import java.time.ZoneOffset

internal class AppointmentAgendaNaftaMapper {

    fun transformOutput(
        transportation: TransportationOptionsResponse,
        advisors: DealerAdvisorResponse,
        response: DealerAgendaNAFTAResponse
    ): AgendaOutput {
        val transform = response.segments
            ?.filter { segment -> segment.slots?.isNotEmpty() == true }
            ?.mapNotNull { agenda ->
                transformDate(agenda.date)?.let { date ->
                    AgendaOutput.DaysItem(
                        date = date,
                        slots = transformSlot(
                            response = agenda.slots,
                            transportations = transportation.transportations,
                            advisors = advisors.advisors
                        )
                    )
                }
            }
        return AgendaOutput(days = transform)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformSlot(
        response: List<Segment.Slot?>?,
        transportations: List<TransportationOption>?,
        advisors: List<ServiceAdvisors>?
    ): List<SlotsItem>? =
        response?.mapNotNull { slot ->
            slot?.let {
                transformTime(slot.time)?.let { time ->
                    AgendaOutput.DaysItem.SlotsItem(
                        start = time,
                        transportationOptions = transformTransportations(
                            slot.transportationOptions,
                            transportations
                        ),
                        serviceAdvisors = transformAdvisors(slot.serviceAdvisors, advisors)
                    )
                }
            }
        }

    @Suppress("SwallowedException")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformDate(dateResponse: Long?): String? =
        dateResponse?.let {
            try {
                Instant.ofEpochMilli(dateResponse)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDate()
                    .toString()
            } catch (e: DateTimeException) {
                null
            }
        }

    @Suppress("SwallowedException")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformTime(time: Long?): String? =
        time?.let {
            try {
                Instant.ofEpochMilli(time)
                    .atZone(ZoneOffset.UTC)
                    .toLocalTime()
                    .toString()
            } catch (e: DateTimeException) {
                null
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformTransportations(
        response: List<TransportationOptionsItem?>?,
        transportations: List<TransportationOption>?
    ): List<SlotsItem.TransportationOptionsItem?>? =
        response?.mapNotNull { item ->
            transportations?.first { it.code == item?.code }
                ?.let { option ->
                    AgendaOutput.DaysItem.SlotsItem.TransportationOptionsItem(
                        code = option.code,
                        description = option.description
                    )
                }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformAdvisors(
        response: List<ServiceAdvisorsItem?>?,
        advisors: List<ServiceAdvisors>?
    ): List<SlotsItem.ServiceAdvisorsItem?>? =
        response?.mapNotNull { advisorItem ->
            advisors?.first { it.memberId == advisorItem?.id }
                ?.let { advisor ->
                    AgendaOutput.DaysItem.SlotsItem.ServiceAdvisorsItem(
                        id = advisor.id,
                        name = advisor.name,
                        memberId = advisor.memberId
                    )
                }
        }
}
