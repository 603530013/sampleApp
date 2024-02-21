package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.agenda

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput.DaysItem
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TransportationOptionsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAdvisorResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse.Segment
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse.Segment.Slot
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse.Segment.Slot.ServiceAdvisorsItem
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse.Segment.Slot.TransportationOptionsItem
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class AppointmentAgendaNaftaMapperTest {

    private val mapper = spyk(AppointmentAgendaNaftaMapper())

    private val transportationResponse = TransportationOptionsResponse(
        transportations = listOf(
            TransportationOptionsResponse.TransportationOption(
                code = "12345",
                description = "testDescription_12345"
            ),
            TransportationOptionsResponse.TransportationOption(code = "6789", description = "testDescription_6789")
        )
    )

    private val advisorsResponse = DealerAdvisorResponse(
        listOf(
            DealerAdvisorResponse.ServiceAdvisors(
                id = 123,
                name = "testAdvisor_123",
                memberId = 1234
            ),
            DealerAdvisorResponse.ServiceAdvisors(
                id = 456,
                name = "testAdvisor_456",
                memberId = 5678
            )
        )
    )

    private val agendaResponse = DealerAgendaNAFTAResponse(
        listOf(
            Segment(
                date = LocalDateTime.of(2023, 11, 20, 9, 30)
                    .toInstant(ZoneOffset.UTC)
                    .truncatedTo(ChronoUnit.DAYS)
                    .toEpochMilli(),
                slots = listOf(
                    Slot(
                        time = LocalDateTime.of(2023, 11, 20, 9, 30)
                            .toInstant(ZoneOffset.UTC)
                            .toEpochMilli(),
                        transportationOptions = listOf(
                            TransportationOptionsItem("12345"),
                            TransportationOptionsItem("6789")
                        ),
                        serviceAdvisors = listOf(
                            ServiceAdvisorsItem(1234),
                            ServiceAdvisorsItem(5678)
                        )
                    ),
                    Slot(
                        time = LocalDateTime.of(2023, 11, 20, 9, 45)
                            .toInstant(ZoneOffset.UTC)
                            .toEpochMilli(),
                        transportationOptions = listOf(
                            TransportationOptionsItem("12345"),
                            TransportationOptionsItem("6789")
                        ),
                        serviceAdvisors = listOf(
                            ServiceAdvisorsItem(1234),
                            ServiceAdvisorsItem(5678)
                        )
                    )
                )
            ),
            Segment(
                date = LocalDateTime.of(2023, 11, 21, 9, 30)
                    .toInstant(ZoneOffset.UTC)
                    .truncatedTo(ChronoUnit.DAYS)
                    .toEpochMilli(),
                slots = listOf(
                    Slot(
                        time = LocalDateTime.of(2023, 11, 21, 9, 30)
                            .toInstant(ZoneOffset.UTC)
                            .toEpochMilli(),
                        transportationOptions = listOf(
                            TransportationOptionsItem("12345"),
                            TransportationOptionsItem("6789")
                        ),
                        serviceAdvisors = listOf(
                            ServiceAdvisorsItem(1234),
                            ServiceAdvisorsItem(5678)
                        )
                    ),
                    Slot(
                        time = LocalDateTime.of(2023, 11, 21, 9, 45)
                            .toInstant(ZoneOffset.UTC)
                            .toEpochMilli(),
                        transportationOptions = listOf(
                            TransportationOptionsItem("12345"),
                            TransportationOptionsItem("6789")
                        ),
                        serviceAdvisors = listOf(
                            ServiceAdvisorsItem(1234),
                            ServiceAdvisorsItem(5678)
                        )
                    )
                )
            )
        )
    )

    private val mapperResponse = AgendaOutput(
        days = listOf(
            DaysItem(
                date = "2023-11-20",
                slots = listOf(
                    DaysItem.SlotsItem(
                        start = "09:30",
                        transportationOptions = listOf(
                            DaysItem.SlotsItem.TransportationOptionsItem(
                                code = "12345",
                                description = "testDescription_12345"
                            ),
                            DaysItem.SlotsItem.TransportationOptionsItem(
                                code = "6789",
                                description = "testDescription_6789"
                            )
                        ),
                        serviceAdvisors = listOf(
                            DaysItem.SlotsItem.ServiceAdvisorsItem(
                                id = 123,
                                name = "testAdvisor_123",
                                memberId = 1234
                            ),
                            DaysItem.SlotsItem.ServiceAdvisorsItem(
                                id = 456,
                                name = "testAdvisor_456",
                                memberId = 5678
                            )
                        )
                    ),
                    DaysItem.SlotsItem(
                        start = "09:45",
                        transportationOptions = listOf(
                            DaysItem.SlotsItem.TransportationOptionsItem(
                                code = "12345",
                                description = "testDescription_12345"
                            ),
                            DaysItem.SlotsItem.TransportationOptionsItem(
                                code = "6789",
                                description = "testDescription_6789"
                            )
                        ),
                        serviceAdvisors = listOf(
                            DaysItem.SlotsItem.ServiceAdvisorsItem(
                                id = 123,
                                name = "testAdvisor_123",
                                memberId = 1234
                            ),
                            DaysItem.SlotsItem.ServiceAdvisorsItem(
                                id = 456,
                                name = "testAdvisor_456",
                                memberId = 5678
                            )
                        )
                    )
                )
            ),
            DaysItem(
                date = "2023-11-21",
                slots = listOf(
                    DaysItem.SlotsItem(
                        start = "09:30",
                        transportationOptions = listOf(
                            DaysItem.SlotsItem.TransportationOptionsItem(
                                code = "12345",
                                description = "testDescription_12345"
                            ),
                            DaysItem.SlotsItem.TransportationOptionsItem(
                                code = "6789",
                                description = "testDescription_6789"
                            )
                        ),
                        serviceAdvisors = listOf(
                            DaysItem.SlotsItem.ServiceAdvisorsItem(
                                id = 123,
                                name = "testAdvisor_123",
                                memberId = 1234
                            ),
                            DaysItem.SlotsItem.ServiceAdvisorsItem(
                                id = 456,
                                name = "testAdvisor_456",
                                memberId = 5678
                            )
                        )
                    ),

                    DaysItem.SlotsItem(
                        start = "09:45",
                        transportationOptions = listOf(
                            DaysItem.SlotsItem.TransportationOptionsItem(
                                code = "12345",
                                description = "testDescription_12345"
                            ),
                            DaysItem.SlotsItem.TransportationOptionsItem(
                                code = "6789",
                                description = "testDescription_6789"
                            )
                        ),
                        serviceAdvisors = listOf(
                            DaysItem.SlotsItem.ServiceAdvisorsItem(
                                id = 123,
                                name = "testAdvisor_123",
                                memberId = 1234
                            ),
                            DaysItem.SlotsItem.ServiceAdvisorsItem(
                                id = 456,
                                name = "testAdvisor_456",
                                memberId = 5678
                            )
                        )
                    )
                )
            )
        )
    )

    @Test
    fun `when execute transformOutput then return AgendaOutput`() {
        val actual = mapper.transformOutput(transportationResponse, advisorsResponse, agendaResponse)
        Assert.assertEquals(mapperResponse, actual)
    }
}
