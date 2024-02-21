package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.agenda

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaEMEAResponse
import java.time.DateTimeException
import java.time.Instant
import java.time.ZoneOffset

internal class AppointmentAgendaEmeaMapper {

    fun transformOutput(response: DealerAgendaEMEAResponse): AgendaOutput {
        val transform = response.agenda
            ?.filter { agenda -> agenda.availableSlot?.isNotEmpty() == true }
            ?.mapNotNull { agenda ->
                transformDate(agenda.date?.toLongOrNull())?.let { date ->
                    AgendaOutput.DaysItem(
                        date = date,
                        slots = agenda.slots
                            ?.mapNotNull { slot ->
                                transformTime(slot.time?.toLongOrNull())?.let { time ->
                                    AgendaOutput.DaysItem.SlotsItem(start = time)
                                }
                            }
                    )
                }
            }
        return AgendaOutput(days = transform)
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
}
