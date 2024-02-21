package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType.Generic
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsEmeaResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetAppointmentDetailsForEmeaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetAppointmentDetailsForEmeaExecutor

    private val input = DetailsInput(
        vin = "testVin",
        id = "testDealerID"
    )

    private val detailsResponse = AppointmentDetailsEmeaResponse(
        vin = "testVin",
        serviceId = "testServiceId",
        dealerId = "testDealerId",
        location = "testLocation",
        scheduledTime = "2023-09-29T12:50:4100Z",
        faultDescription = "testFaultDescription",
        servicesList = listOf(AppointmentDetailsEmeaResponse.Service("testServiceList")),
        telephone = "testTelephone",
        vehicleKm = "200000",
        status = AppointmentStatusResponse.Booked
    )

    private val detailsOutput = DetailsOutput(
        id = "testServiceId",
        vin = "testVin",
        bookingId = "testDealerId",
        bookingLocation = "testLocation",
        scheduledTime = "2023-09-29T12:50:00.410Z",
        comment = "testFaultDescription",
        mileage = "200000",
        email = null,
        phone = "testTelephone",
        status = Status.Booked,
        reminders = emptyList(),
        services = listOf(DetailsOutput.Service(id = "testServiceList", name = "null", type = Generic))
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetAppointmentDetailsForEmeaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute then make a network API call with success response`() {
        val dealerInput = DetailsInput(
            vin = "testVin",
            id = "testDealerID"
        )
        every { executor.params(any()) } returns dealerInput
        coEvery { communicationManager.get<AppointmentDetailsEmeaResponse>(any(), any()) } returns
            Success(detailsResponse)
        runTest {
            val response = executor.execute(dealerInput)
            verify {
                executor.request(
                    type = eq(AppointmentDetailsEmeaResponse::class.java),
                    urls = eq(
                        arrayOf(
                            "/v1/accounts/",
                            uid,
                            "/vehicles/",
                            dealerInput.vin,
                            "/servicescheduler/appointment/",
                            dealerInput.id
                        )
                    )
                )
            }
            coVerify {
                communicationManager.get<AppointmentDetailsEmeaResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(detailsOutput.toString(), success.response.toString())
        }
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf(Constants.Input.ID to "testDealerId")
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing id then throw missing parameter`() {
        val input = mapOf(Constants.Input.VIN to "testVin")
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with right inputs then return an DealerAppointmentInput`() {
        val params = mapOf(
            Constants.PARAM_ID to input.id,
            Constants.PARAM_VIN to input.vin
        )
        val dealerAppointmentInput = executor.params(params)
        Assert.assertEquals(input, dealerAppointmentInput)
    }
}
