package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.Brand.JEEP
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.Market.NAFTA
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token.NaftaTokenManager
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history.AppointmentHistoryNaftaMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryNaftaInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput.Appointment
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryNAFTAResponse
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

internal class GetHistoryForNaftaFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetHistoryForNaftaFcaExecutor

    private val historyOutput =
        HistoryOutput(
            appointments = listOf(
                Appointment("serviceIdTest", "2023-09-29T12:50:4100Z", Status.Requested)
            )
        )

    private val appointmentResponse = AppointmentHistoryNAFTAResponse(
        listOf(
            AppointmentHistoryNAFTAResponse.Appointment(
                dealerId = "dealerIdTest",
                appointmentId = "serviceIdTest",
                scheduledTime = 1656414000000,
                status = AppointmentStatusResponse.Booked
            )
        )
    )

    override fun setup() {
        super.setup()
        mockkObject(BookingOnlineCache)
        mockkConstructor(AppointmentHistoryNaftaMapper::class)
        every { anyConstructed<AppointmentHistoryNaftaMapper>().transformOutput(any()) } returns historyOutput
        every { configurationManager.brand } returns JEEP
        every { configurationManager.market } returns NAFTA
        mockkConstructor(NaftaTokenManager::class)
        every { anyConstructed<NaftaTokenManager>().getOssTokenFromCache(any()) } returns "testToken"
        executor = spyk(GetHistoryForNaftaFcaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute with empty params then throw missing bookingId param error`() {
        val executor = GetHistoryForNaftaFcaExecutor(middlewareComponent)
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            val output = executor.params()
            Assert.assertEquals(exception, output)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with missing bookingId then throw missing params`() {
        val input = mapOf<String, Any>()
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with invalid bookingId then throw invalid params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to 7019500
        )

        val exception = PIMSFoundationError.invalidParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with missing bookingLocation then throw missing params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "7019500"
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with missing vin then throw missing params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "7019500"
        )

        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with invalid vin then throw invalid params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "7019500",
            Constants.Input.VIN to 7019500
        )

        val exception = PIMSFoundationError.invalidParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with valid params then return valid model`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "7019500",
            Constants.Input.VIN to "7019500"
        )

        val result = executor.params(input)
        Assert.assertEquals("7019500", result.dealerId)
    }

    @Test
    fun `when execute then make a get API call`() {
        val input = HistoryNaftaInput(dealerId = "dealerIdTest", vin = "testVin")
        every { executor.params(any()) } returns input

        coEvery {
            communicationManager.get<AppointmentHistoryNAFTAResponse>(any(), any())
        } returns NetworkResponse.Success(appointmentResponse)

        coJustRun { BookingOnlineCache.write(any<AppointmentHistoryNAFTAResponse>()) }

        runTest {
            val response = executor.execute(input, "testToken")

            verify {
                executor.request(
                    type = eq(AppointmentHistoryNAFTAResponse::class.java),
                    urls = eq(
                        arrayOf("/v1/accounts/testCustomerId/vehicles/testVin/servicescheduler/appointments")
                    ),
                    headers = mapOf("dealer-authorization" to "testToken")
                )
            }

            coVerify(exactly = 1) {
                communicationManager.get<AppointmentHistoryNAFTAResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }

            coVerify(exactly = 1) { BookingOnlineCache.write(appointmentResponse) }
            coVerify(exactly = 1) { anyConstructed<AppointmentHistoryNaftaMapper>().transformOutput(any()) }

            Assert.assertEquals(true, response is Success)
        }
    }
}
