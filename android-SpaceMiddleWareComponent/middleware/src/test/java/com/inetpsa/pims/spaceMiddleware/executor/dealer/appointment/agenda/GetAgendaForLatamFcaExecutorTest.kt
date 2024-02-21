package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.agenda

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.asJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput.TimeFence.MONTH
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse.Segment
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse.Segment.Slot
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse.Segment.Slot.TransportationOption
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

internal class GetAgendaForLatamFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetAgendaForLatamFcaExecutor
    private val startDate = LocalDate.of(2023, 7, 20).toString()
    private val response = DealerAgendaLATAMResponse(
        segments = listOf(
            Segment(
                date = 1698762600000,
                slots = listOf(
                    Slot(
                        slotId = 123456789,
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

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetAgendaForLatamFcaExecutor(middlewareComponent, emptyMap()))
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
        val input = mapOf(Constants.Input.Appointment.BOOKING_ID to 123)

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
            Constants.Input.Appointment.START_DATE to "60827"
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
    fun `when execute with missing servicesIds then do not throw exception`() = runTest {
        val input = AgendaInput(
            dealerId = "7019500|0",
            startDate = LocalDate.of(2023, 7, 20),
            timeFence = MONTH,
            vin = "testVin",
            serviceIds = null
        )

        coEvery {
            communicationManager.get<DealerAgendaLATAMResponse>(any(), any())
        } returns NetworkResponse.Success(response)

        executor.execute(input)

        coVerify(exactly = 1) { communicationManager.get<DealerAgendaLATAMResponse>(any(), any()) }
    }

    @Test
    fun `when execute with valid input then do not throw exception`() = runTest {
        val input = AgendaInput(
            dealerId = "7019500|0",
            startDate = LocalDate.of(2023, 7, 20),
            timeFence = MONTH,
            vin = "testVin",
            serviceIds = null
        )

        coEvery {
            communicationManager.get<DealerAgendaLATAMResponse>(any(), any())
        } returns NetworkResponse.Success(response)

        executor.execute(input)

        coVerify(exactly = 1) { communicationManager.get<DealerAgendaLATAMResponse>(any(), any()) }
    }

    @Test
    fun `when execute then make a get API call`() {
        val input = AgendaInput(
            dealerId = "7019500",
            dealerLocation = "0",
            startDate = LocalDate.of(2023, 7, 20),
            timeFence = MONTH,
            vin = "testVin",
            serviceIds = ""
        )

        every { executor.params(any()) } returns input

        coEvery { communicationManager.get<DealerAgendaLATAMResponse>(any(), any()) } returns Success(response)

        runTest {
            val response = executor.execute(input)

            val queries = mapOf(
                Constants.QUERY_PARAM_KEY_START_DATE to 1689822000000L.toString(),
                Constants.QUERY_PARAM_KEY_END_DATE to 1692500400000L.toString(),
                Constants.QUERY_PARAM_KEY_SERVICES_IDS to "",
                Constants.QUERY_PARAM_KEY_DEALER_ID to "7019500"
            )

            verify(exactly = 1) {
                executor.request(
                    type = object : TypeToken<DealerAgendaLATAMResponse>() {}.type,
                    urls = arrayOf("/v1/servicescheduler/timesegments"),
                    queries = match { it.asJson().equals(queries.asJson(), true) }
                )
            }

            coVerify(exactly = 1) {
                communicationManager.get<DealerAgendaLATAMResponse>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            Assert.assertEquals(true, response is Success)
        }
    }
}
