package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand.MASERATI
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.mmx.foundation.tools.Market.LATAM
import com.inetpsa.mmx.foundation.tools.Market.NAFTA
import com.inetpsa.mmx.foundation.tools.Market.NONE
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.agenda.GetAgendaForEmeaFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.agenda.GetAgendaForLatamFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.agenda.GetAgendaForMaseratiFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.agenda.GetAgendaForNaftaFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.department.GetNaftaDepartmentIdExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput.DaysItem
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput.DaysItem.SlotsItem
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TransportationOptionsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TransportationOptionsResponse.TransportationOption
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAdvisorResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAdvisorResponse.ServiceAdvisors
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaEMEAResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaEMEAResponse.Agenda
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaEMEAResponse.Agenda.Slot
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaMaseratiResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetDealerAgendaFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetDealerAgendaFcaExecutor

    private val emeaResponse = DealerAgendaEMEAResponse(
        brand = "_",
        location = "",
        dealerId = "7019500",
        agenda = listOf(
            Agenda(
                date = "1702080000000",
                slots = listOf(
                    Slot(
                        slot = "3",
                        time = "1702193400000",
                        limit = "1",
                        reservation = "1"
                    ),
                    Slot(
                        slot = "1",
                        time = "1702194300000",
                        limit = "1",
                        reservation = "3"
                    )
                )
            ),
            Agenda(
                date = "1702166400000",
                slots = listOf(
                    Slot(
                        slot = "1",
                        time = "1702193400000",
                        limit = "1",
                        reservation = "2"
                    ),
                    Slot(
                        slot = "1",
                        time = "1702194300000",
                        limit = "1",
                        reservation = "2"
                    )
                )
            )
        )
    )

    private val maseratiResponse = DealerAgendaMaseratiResponse(
        dealerId = "7019500",
        agenda = listOf(
            DealerAgendaMaseratiResponse.Agenda(
                date = "2023-07-20",
                slots = listOf(
                    DealerAgendaMaseratiResponse.Agenda.Slot(time = "00:00"),
                    DealerAgendaMaseratiResponse.Agenda.Slot(time = "00:15")
                )
            ),
            DealerAgendaMaseratiResponse.Agenda(
                date = "2023-07-21",
                slots = listOf(
                    DealerAgendaMaseratiResponse.Agenda.Slot(time = "00:00"),
                    DealerAgendaMaseratiResponse.Agenda.Slot(time = "00:15")
                )
            )
        )
    )

    private val latamResponse = DealerAgendaLATAMResponse(
        segments = listOf(
            DealerAgendaLATAMResponse.Segment(
                date = 1698762600000,
                slots = listOf(
                    DealerAgendaLATAMResponse.Segment.Slot(
                        slotId = null,
                        time = 1698762600000,
                        transportationOptions = listOf(
                            DealerAgendaLATAMResponse.Segment.Slot.TransportationOption(
                                code = "SHUTTLE"
                            )
                        )
                    )

                )
            )
        )
    )

    private val naftaResponse = DealerAgendaNAFTAResponse(
        listOf(
            DealerAgendaNAFTAResponse.Segment(
                date = 1698762600000,
                slots = listOf(
                    DealerAgendaNAFTAResponse.Segment.Slot(
                        time = 1698762600000
                    )
                )
            )
        )
    )

    private val expectedNaftaAgendaOutput = AgendaOutput(
        days = listOf(
            DaysItem(
                date = "2023-10-31",
                listOf(SlotsItem(start = "14:30"))
            )
        )
    )
    private val expectedLatamAgendaOutput = AgendaOutput(
        days = listOf(
            DaysItem(
                date = "2023-10-31",
                slots = listOf(
                    SlotsItem(start = "14:30")
                )
            )
        )
    )
    private val expectedEMEAAgendaOutput = AgendaOutput(
        days = listOf(
            DaysItem(
                date = "2023-12-09",
                slots = listOf(
                    SlotsItem(start = "07:30"),
                    SlotsItem(start = "07:45")
                )
            ),
            DaysItem(
                date = "2023-12-10",
                slots = listOf(
                    SlotsItem(start = "07:30"),
                    SlotsItem(start = "07:45")
                )
            )
        )
    )
    private val expectedMaseratiAgendaOutput = AgendaOutput(
        days = listOf(
            DaysItem(
                date = "2023-07-20",
                slots = listOf(
                    SlotsItem(start = "00:00"),
                    SlotsItem(start = "00:15")
                )
            ),
            DaysItem(
                date = "2023-07-21",
                slots = listOf(
                    SlotsItem(start = "00:00"),
                    SlotsItem(start = "00:15")
                )
            )
        )
    )

    private val responseDealerOutput = AgendaOutput(
        rrdi = "testRrdi",
        period = 2,
        from = "testFrom",
        to = "testTo",
        type = 1,
        days = listOf(
            DaysItem(
                date = "testDate",
                slots = listOf(
                    SlotsItem(
                        receptionTotal = 1,
                        start = "testStart",
                        discount = 1F,
                        end = "testEnd",
                        receptionAvailable = 1
                    )
                )
            )
        )
    )

    private val transportationResponse = TransportationOptionsResponse(
        transportations = listOf(
            TransportationOption(
                code = "12345",
                description = "testDescription_12345"
            ),
            TransportationOption(code = "6789", description = "testDescription_6789")
        )
    )

    private val advisorsResponse = DealerAdvisorResponse(
        listOf(
            ServiceAdvisors(
                id = 123,
                name = "testAdvisor_123",
                memberId = 1234
            ),
            ServiceAdvisors(
                id = 456,
                name = "testAdvisor_456",
                memberId = 5678
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        every { middlewareComponent.dataManager } returns dataManager
        every { middlewareComponent.userSessionManager } returns userSessionManager
        every { userSessionManager.getUserSession() } returns userSession
        every { userSessionManager.getUserSession()?.customerId } returns "testCustomerId"
        executor = spyk(GetDealerAgendaFcaExecutor(baseCommand))
        mockkConstructor(GetAgendaForLatamFcaExecutor::class)
        mockkConstructor(GetAgendaForNaftaFcaExecutor::class)
        mockkConstructor(GetAgendaForEmeaFcaExecutor::class)
        // coEvery { executor.saveOnCache(dealerAgendaLATAMResponse) }
        // val successResponse = NetworkResponse.Success(latamResponse)
        // coEvery { anyConstructed<GetAgendaForLatamFcaExecutor>().execute(any()) } returns successResponse
    }

    @Test
    fun `when execute For FCA with brand Maserati then call handleMaseratiFlow()`() =
        runTest {
            val input = mapOf(
                Constants.Input.Appointment.BOOKING_ID to "7019500",
                Constants.Input.Appointment.START_DATE to "2023-07-20",
                Constants.Input.Appointment.TIME_FENCE to "month",
                Constants.Input.VIN to "testVin",
                Constants.Input.Appointment.SERVICES to ""
            )

            every { executor.params(any()) } returns input
            every { configurationManager.brand } returns MASERATI
            coEvery {
                communicationManager.get<DealerAgendaMaseratiResponse>(any(), any())
            } returns NetworkResponse.Success(maseratiResponse)

            executor.execute(input)

            coVerify(exactly = 1) { executor.handleMaseratiFlow(any()) }
        }

    @Test
    fun `when execute For FCA with market EMEA then call handleEmeaFlow()`() =
        runTest {
            val input = mapOf(
                Constants.Input.Appointment.BOOKING_ID to "7019500",
                Constants.Input.Appointment.START_DATE to "2023-07-20",
                Constants.Input.Appointment.TIME_FENCE to "month",
                Constants.Input.VIN to "testVin",
                Constants.Input.Appointment.SERVICES to ""
            )

            every { executor.params(any()) } returns input
            every { configurationManager.market } returns EMEA
            coEvery {
                communicationManager.get<DealerAgendaEMEAResponse>(any(), any())
            } returns NetworkResponse.Success(emeaResponse)

            executor.execute(input)

            coVerify(exactly = 1) { executor.handleEmeaFlow(any()) }
        }

    @Test
    fun `when execute For FCA with market LATAM then call handleLatamFlow()`() =
        runTest {
            val input = mapOf(
                Constants.Input.Appointment.BOOKING_ID to "7019500",
                Constants.Input.Appointment.START_DATE to "2023-07-20",
                Constants.Input.Appointment.TIME_FENCE to "month",
                Constants.Input.VIN to "testVin",
                Constants.Input.Appointment.SERVICES to ""
            )
            every { executor.params(any()) } returns input
            every { configurationManager.market } returns LATAM

            coEvery {
                executor.handleLatamFlow(any())
                // communicationManager.get<DealerAgendaLATAMResponse>(any(), any())
            } returns NetworkResponse.Success(responseDealerOutput)

            executor.execute(input)
            coVerify(exactly = 1) { executor.handleLatamFlow(any()) }
        }

    @Test
    fun `when execute For FCA with market NAFTA then call handleNaftaFlow()`() =
        runTest {
            val input = mapOf(
                Constants.Input.Appointment.BOOKING_ID to "7019500",
                Constants.Input.Appointment.START_DATE to "2023-07-20",
                Constants.Input.Appointment.TIME_FENCE to "month",
                Constants.Input.VIN to "testVin",
                Constants.Input.Appointment.SERVICES to ""
            )

            every { executor.params(any()) } returns input
            every { configurationManager.market } returns NAFTA

            coEvery {
                executor.handleNaftaFlow(any())
            } returns NetworkResponse.Success(responseDealerOutput)

            executor.execute(input)
            coVerify(exactly = 1) { executor.handleNaftaFlow(any()) }
        }

    @Test
    fun `when execute For FCA with invalid market then throw PimsError exception`() {
        runTest {
            every { configurationManager.market } returns NONE
            val exception = PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)

            try {
                executor.execute()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `test when market is other than Emea,Latam,Nafta execute() throws error`() {
        val error = PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)
        every { configurationManager.market } returns NONE
        runBlocking {
            try {
                executor.execute(emptyMap())
            } catch (pimsError: PIMSError) {
                Assert.assertEquals(error.code, pimsError.code)
                Assert.assertEquals(error.message, pimsError.message)
            }
        }
    }

    @Test
    fun `test handleNaftaFlow() returns success response`() {
        val input = mutableMapOf<String, Any>(
            Constants.Input.Appointment.BOOKING_ID to "7019500"
        )
        mockkConstructor(GetNaftaDepartmentIdExecutor::class)

        mockkConstructor(GetAdvisorForNaftaFcaExecutor::class)
        val advisorResponse = NetworkResponse.Success(advisorsResponse)
        coEvery { anyConstructed<GetAdvisorForNaftaFcaExecutor>().execute(any()) } returns advisorResponse
        coEvery { anyConstructed<GetAdvisorForNaftaFcaExecutor>().execute(any(), any()) } returns advisorResponse
        coEvery { anyConstructed<GetAdvisorForNaftaFcaExecutor>().execute(any(), any(), any()) } returns advisorResponse

        mockkConstructor(GetTransportationOptionsForNaftaFCAExecutor::class)
        val transportationsResponse = NetworkResponse.Success(transportationResponse)
        coEvery {
            anyConstructed<GetTransportationOptionsForNaftaFCAExecutor>().execute(any())
        } returns transportationsResponse
        coEvery {
            anyConstructed<GetTransportationOptionsForNaftaFCAExecutor>().execute(
                any(),
                any()
            )
        } returns transportationsResponse
        coEvery {
            anyConstructed<GetTransportationOptionsForNaftaFCAExecutor>().execute(
                any(),
                any(),
                any()
            )
        } returns transportationsResponse

        mockkConstructor(GetAgendaForNaftaFcaExecutor::class)
        coEvery {
            GetNaftaDepartmentIdExecutor(middlewareComponent, input).execute()
        } returns NetworkResponse.Success(6414)
        input[Constants.Input.Appointment.DEPARTMENT_ID] = 6414
        coEvery {
            GetAgendaForNaftaFcaExecutor(middlewareComponent, input).execute()
        } returns NetworkResponse.Success(naftaResponse)

        runBlocking {
            val response = executor.handleNaftaFlow(input)
            coVerify {
                GetAgendaForNaftaFcaExecutor(middlewareComponent, input).execute()
            }
            Assert.assertEquals(expectedNaftaAgendaOutput, (response as NetworkResponse.Success).response)
        }
    }

    @Test
    fun `test handleEmeaFlow() returns success response`() {
        val input = mutableMapOf<String, Any>(
            Constants.Input.Appointment.BOOKING_ID to "7019500"
        )
        mockkConstructor(GetAgendaForEmeaFcaExecutor::class)
        coEvery {
            GetAgendaForEmeaFcaExecutor(middlewareComponent, input)
                .execute()
        } returns NetworkResponse.Success(emeaResponse)

        runBlocking {
            val response = executor.handleEmeaFlow(input)
            coVerify {
                GetAgendaForEmeaFcaExecutor(middlewareComponent, input).execute()
            }
            Assert.assertEquals(expectedEMEAAgendaOutput, (response as NetworkResponse.Success).response)
        }
    }

    @Test
    fun `test handleMaseratiFlow() returns success response`() {
        val input = mutableMapOf<String, Any>(
            Constants.Input.Appointment.BOOKING_ID to "7019500"
        )
        mockkConstructor(GetAgendaForMaseratiFcaExecutor::class)
        coEvery {
            GetAgendaForMaseratiFcaExecutor(middlewareComponent, input)
                .execute()
        } returns NetworkResponse.Success(maseratiResponse)

        runBlocking {
            val response = executor.handleMaseratiFlow(input)
            coVerify {
                GetAgendaForMaseratiFcaExecutor(middlewareComponent, input).execute()
            }
            Assert.assertEquals(expectedMaseratiAgendaOutput, (response as NetworkResponse.Success).response)
        }
    }

    @Test
    fun `test handleLatamFlow() returns success response`() {
        val input = mutableMapOf<String, Any>(
            Constants.Input.Appointment.BOOKING_ID to "7019500"
        )
        mockkConstructor(GetAgendaForLatamFcaExecutor::class)
        coEvery {
            GetAgendaForLatamFcaExecutor(middlewareComponent, input)
                .execute()
        } returns NetworkResponse.Success(latamResponse)

        runBlocking {
            val response = executor.handleLatamFlow(input)
            coVerify {
                GetAgendaForLatamFcaExecutor(middlewareComponent, input).execute()
            }
            Assert.assertEquals(expectedLatamAgendaOutput, (response as NetworkResponse.Success).response)
        }
    }

    @Test
    fun `test params() returns empty map when params are null`() {
        val params = executor.params(null)
        Assert.assertEquals(emptyMap<String, Any?>(), params)
    }

    @Test
    fun `test params() returns params`() {
        val input = mapOf<String, Any>(
            Constants.Input.Appointment.BOOKING_ID to "7019500"
        )
        val params = executor.params(input)
        Assert.assertEquals(input, params)
        Assert.assertEquals(
            input[Constants.Input.Appointment.BOOKING_ID],
            params[Constants.Input.Appointment.BOOKING_ID]
        )
    }
}
