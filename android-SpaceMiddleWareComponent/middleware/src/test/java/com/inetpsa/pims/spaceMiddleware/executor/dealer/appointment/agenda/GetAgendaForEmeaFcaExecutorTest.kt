package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.agenda

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand.MASERATI
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput.TimeFence.MONTH
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaEMEAResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaEMEAResponse.Agenda
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaEMEAResponse.Agenda.Slot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

internal class GetAgendaForEmeaFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetAgendaForEmeaFcaExecutor
    private val startDate = LocalDate.of(2023, 7, 20).toString()
    private val response = DealerAgendaEMEAResponse(
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

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetAgendaForEmeaFcaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute with missing dealerId then throw missing params`() {
        val input = mapOf(Constants.Input.Appointment.START_DATE to startDate)

        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with dealerId blank then throw missing params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "",
            Constants.Input.Appointment.START_DATE to startDate,
            Constants.Input.Appointment.TIME_FENCE to "MONTH",
            Constants.Input.VIN to "VR279271982"
        )

        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with invalid dealerId then throw invalid params`() {
        val input = mapOf(Constants.Input.Appointment.BOOKING_ID to 20230720)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with missing start then throw missing params`() {
        val input = mapOf(Constants.Input.Appointment.BOOKING_ID to "60827")

        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.START_DATE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with invalid start then throw invalid params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "60827",
            Constants.Input.Appointment.START_DATE to 234567
        )

        val exception = PIMSFoundationError.invalidParameter(Constants.Input.Appointment.START_DATE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with missing timeFence then throw missing params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "60827",
            Constants.Input.Appointment.START_DATE to startDate
        )

        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.TIME_FENCE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with invalid timeFence then throw invalid params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "60827",
            Constants.Input.Appointment.START_DATE to startDate,
            Constants.Input.Appointment.TIME_FENCE to "DAY"
        )

        val exception = PIMSFoundationError.invalidParameter(Constants.Input.Appointment.TIME_FENCE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with valid input then execute parseDate`() = runTest {
        val input = AgendaInput(
            dealerId = "7019500|0",
            startDate = LocalDate.of(2023, 7, 20),
            timeFence = MONTH,
            vin = "testVin",
            serviceIds = ""
        )

        every { executor.params(any()) } returns input
        coEvery {
            communicationManager.get<DealerAgendaEMEAResponse>(any(), any())
        } returns NetworkResponse.Success(response)

        executor.execute(input)
    }

    @Test
    fun `when execute with missing servicesIds then do not throw exception`() = runTest {
        val input = AgendaInput(
            dealerId = "7019500|0",
            startDate = LocalDate.of(2023, 7, 20),
            timeFence = MONTH,
            vin = "testVin",
            serviceIds = null
        )

        coEvery {
            communicationManager.get<DealerAgendaEMEAResponse>(any(), any())
        } returns NetworkResponse.Success(response)

        executor.execute(input)

        coVerify(exactly = 1) { communicationManager.get<DealerAgendaEMEAResponse>(any(), any()) }
    }

    @Test
    fun `when execute with missing ossDealer then do not throw exception`() = runTest {
        val input = AgendaInput(
            dealerId = "7019500|0",
            startDate = LocalDate.of(2023, 7, 20),
            timeFence = MONTH,
            vin = "testVin",
            serviceIds = null
        )

        coEvery {
            communicationManager.get<DealerAgendaEMEAResponse>(any(), any())
        } returns NetworkResponse.Success(response)

        executor.execute(input)

        coVerify(exactly = 1) { communicationManager.get<DealerAgendaEMEAResponse>(any(), any()) }
    }

    @Test
    fun `when execute then make a get API call`() {
        val input = AgendaInput(
            dealerId = "7019500",
            dealerLocation = "0",
            startDate = LocalDate.of(2023, 7, 20),
            timeFence = MONTH,
            vin = "testVin",
            serviceIds = null
        )

        every { executor.params(any()) } returns input

        every { configurationManager.brand } returns MASERATI
        coEvery {
            communicationManager.get<DealerAgendaEMEAResponse>(any(), any())
        } returns NetworkResponse.Success(response)

        runTest {
            val response = executor.execute()

            val queries = mapOf(
                Constants.QUERY_PARAM_KEY_DEALER_ID to input.dealerId.orEmpty(),
                Constants.QUERY_PARAM_KEY_DATE to input.startDateMilliSeconds.toString(),
                Constants.QUERY_PARAM_TIME_FENCE to input.timeFence.name.lowercase(),
                Constants.QUERY_PARAM_KEY_LOCATION to input.dealerLocation.orEmpty()
            )

            verify {
                executor.request(
                    type = eq(DealerAgendaEMEAResponse::class.java),
                    urls = eq(
                        arrayOf("/v1/accounts/", "testCustomerId", "/vehicles/", "testVin", "/servicescheduler/agenda")
                    ),
                    queries = eq(queries)
                )
            }

            coVerify(exactly = 1) {
                communicationManager.get<DealerAgendaEMEAResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
        }
    }
}
