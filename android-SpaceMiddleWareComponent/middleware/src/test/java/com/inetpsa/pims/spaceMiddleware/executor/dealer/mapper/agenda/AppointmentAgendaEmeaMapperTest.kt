package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.agenda

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput.DaysItem
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput.DaysItem.SlotsItem
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaEMEAResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaEMEAResponse.Agenda
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaEMEAResponse.Agenda.Slot
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

class AppointmentAgendaEmeaMapperTest {

    private val mapper = spyk(AppointmentAgendaEmeaMapper())

    @Test
    fun `when execute transformToAgendaOutput for EMEA return AgendaOutput`() {
        val response = DealerAgendaEMEAResponse(
            brand = "_",
            location = "",
            dealerId = "7019500",
            agenda = listOf(
                Agenda(
                    date = "1702080000000",
                    slots = listOf(
                        Slot(slot = "3", time = "1702193400000", limit = "1", reservation = "1"),
                        Slot(slot = "1", time = "1702194300000", limit = "1", reservation = "3")
                    )
                ),
                Agenda(
                    date = "1702166400000",
                    slots = listOf(
                        Slot(slot = "1", time = "1702193400000", limit = "1", reservation = "2"),
                        Slot(slot = "1", time = "1702194300000", limit = "1", reservation = "2")
                    )
                )
            )
        )
        val expected = AgendaOutput(
            days = listOf(
                DaysItem(
                    date = "2023-12-09",
                    slots = listOf(
                        SlotsItem(start = "07:30", end = "null"),
                        SlotsItem(start = "07:45", end = "null")
                    )
                ),
                DaysItem(
                    date = "2023-12-10",
                    slots = listOf(
                        SlotsItem(start = "07:30", end = "null"),
                        SlotsItem(start = "07:45", end = "null")
                    )
                )
            )
        )
        val actual = mapper.transformOutput(response)
        Assert.assertEquals(expected.toString(), actual.toString())
    }
}
