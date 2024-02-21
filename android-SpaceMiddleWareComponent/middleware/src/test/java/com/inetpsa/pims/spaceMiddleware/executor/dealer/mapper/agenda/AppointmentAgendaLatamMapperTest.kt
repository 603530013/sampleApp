package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.agenda

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput.DaysItem
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput.DaysItem.SlotsItem
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse.Segment
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse.Segment.Slot
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse.Segment.Slot.TransportationOption
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

class AppointmentAgendaLatamMapperTest {

    private val mapper = spyk(AppointmentAgendaLatamMapper())

    @Test
    fun `when execute transformToAgendaOutput for LATAM then return AgendaOutput`() {
        val response = DealerAgendaLATAMResponse(
            segments = listOf(
                Segment(
                    date = 1698762600000,
                    slots = listOf(
                        Slot(
                            slotId = null,
                            time = 1698762600000,
                            transportationOptions = listOf(
                                TransportationOption(
                                    code = "SHUTTLE"
                                )
                            )
                        )
                    )
                )
            )
        )

        val expected = AgendaOutput(
            days = listOf(
                DaysItem(
                    date = "2023-10-31",
                    slots = listOf(
                        SlotsItem(
                            start = "14:30",
                            end = "null"
                        )
                    )
                )
            )
        )

        val actual = mapper.transformOutput(response)
        Assert.assertEquals(expected.toString(), actual.toString())
    }
}
