package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.Brand.FIAT
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history.AppointmentHistoryEmeaMapper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput.Appointment
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryEMEAResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

internal class GetHistoryForEmeaFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetHistoryForEmeaFcaExecutor

    private val historyOutput =
        HistoryOutput(
            appointments = listOf(
                Appointment("serviceIdTest", "2023-09-29T12:50:4100Z", Status.Requested)
            )
        )

    private val response = AppointmentHistoryEMEAResponse(
        listOf(
            AppointmentHistoryEMEAResponse.Appointment(
                dealerId = "dealerIdTest",
                faultDescription = "",
                reminders = listOf(),
                scheduledTime = "2023-09-29T12:50:4100Z",
                serviceId = "serviceIdTest",
                servicesList = listOf(),
                status = AppointmentStatusResponse.Booked,
                telephone = null,
                vehicleKm = null,
                vin = null
            )
        )
    )

    override fun setup() {
        super.setup()
        mockkConstructor(AppointmentHistoryEmeaMapper::class)
        every {
            anyConstructed<AppointmentHistoryEmeaMapper>().transformOutput(any<AppointmentHistoryEMEAResponse>())
        } returns historyOutput
        every { configurationManager.brand } returns FIAT
        every { configurationManager.market } returns EMEA
        executor = spyk(GetHistoryForEmeaFcaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute params with right input then return a Vin`() {
        val params = "testVin"
        val input = mapOf(Constants.PARAM_VIN to params)
        val output = executor.params(input)
        Assert.assertEquals(params, output)
    }

    @Test
    fun `when execute with empty params then throw missing parameter`() {
        val executor = GetHistoryForEmeaFcaExecutor(middlewareComponent)
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            val output = executor.params()
            Assert.assertEquals(exception, output)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf<String, Any?>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw missing parameter`() {
        val vin = 123
        val input = mapOf(Constants.PARAM_VIN to vin)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute then make a get API call`() {
        val input = "testVin"
        every { executor.params(any()) } returns input
        coEvery {
            communicationManager.get<AppointmentHistoryEMEAResponse>(any(), any())
        } returns NetworkResponse.Success(response)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(AppointmentHistoryEMEAResponse::class.java),
                    urls = eq(
                        arrayOf("/v1/accounts/testCustomerId/vehicles/testVin/servicescheduler/appointment/history")
                    )
                )
            }

            coVerify(exactly = 1) {
                communicationManager.get<AppointmentHistoryEMEAResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }

            coVerify(exactly = 1) {
                anyConstructed<AppointmentHistoryEmeaMapper>().transformOutput(any<AppointmentHistoryEMEAResponse>())
            }
            Assert.assertEquals(true, response is Success)
        }
    }

    @Test
    fun `when execute with input then return HistoryOutput`() {
        val input = "testVin"
        every { executor.params(any()) } returns input
        coEvery {
            communicationManager.get<AppointmentHistoryEMEAResponse>(any(), any())
        } returns NetworkResponse.Success(response)

        runTest {
            val response = executor.execute(input)

            coVerify(exactly = 1) {
                anyConstructed<AppointmentHistoryEmeaMapper>().transformOutput(any<AppointmentHistoryEMEAResponse>())
            }
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(historyOutput, success.response)
        }
    }
}
