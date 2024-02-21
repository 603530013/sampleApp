package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.agenda

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse
import java.time.DateTimeException
import java.time.Instant
import java.time.ZoneOffset

internal class AppointmentAgendaLatamMapper {

    fun transformOutput(response: DealerAgendaLATAMResponse): AgendaOutput {
        val transform = response.segments
            ?.filter { segment -> segment.slots?.isNotEmpty() == true }
            ?.mapNotNull { segment ->
                transformDate(segment.date)?.let { date ->
                    AgendaOutput.DaysItem(
                        date = date,
                        slots = segment.slots?.mapNotNull { slot ->
                            transformTime(slot.time)?.let { time ->
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
    internal fun transformDate(date: Long?): String? =
        date?.let {
            try {
                Instant.ofEpochMilli(date)
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
