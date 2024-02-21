package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Failure
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType.Generic
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status.Requested
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsMaseratiResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetAppointmentDetailsForMaseratiExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetAppointmentDetailsForMaseratiExecutor

    private val input = DetailsInput(
        id = "testAppointmentId",
        vin = "testVin"
    )

    private val detailsOutput = DetailsOutput(
        id = "testServiceId",
        vin = "testVin",
        bookingId = "testDealerId",
        bookingLocation = "testLocation",
        scheduledTime = "null",
        comment = "testFaultDescription",
        mileage = "testVehicleKm",
        email = "null",
        phone = "testTelephone",
        status = Requested,
        services = listOf(
            DetailsOutput.Service(
                id = "testCode",
                type = Generic,
                name = "testDescription",
                price =
                null
            )
        ),
        amount = null,
        reminders = emptyList()
    )

    private val appointmentDetailsMaseratiResponse = AppointmentDetailsMaseratiResponse(
        vin = "testVin",
        serviceId = "testServiceId",
        dealerId = "testDealerId",
        location = "testLocation",
        scheduledTime = "1970-01-01T00:02:03.456Z",
        faultDescription = "testFaultDescription",
        servicesList = listOf(
            AppointmentDetailsMaseratiResponse.Service(
                code = "testCode",
                description = "testDescription"
            )
        ),
        telephone = "testTelephone",
        vehicleKm = "testVehicleKm",
        status = AppointmentStatusResponse.Requested,
        reminders = listOf("testReminders")
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetAppointmentDetailsForMaseratiExecutor(middlewareComponent))
    }

    @Test
    fun `when execute then make a network API call with success response`() {
        val dealerInput = DetailsInput(
            id = "testAppointmentId",
            vin = "testVin"
        )
        every { executor.params(any()) } returns dealerInput
        coEvery { communicationManager.get<AppointmentDetailsMaseratiResponse>(any(), any()) } returns
            Success(appointmentDetailsMaseratiResponse)

        runTest {
            val response = executor.execute(dealerInput)
            verify {
                executor.request(
                    type = eq(AppointmentDetailsMaseratiResponse::class.java),
                    urls = eq(
                        arrayOf(
                            "/v1/accounts/",
                            uid,
                            "/vehicles/",
                            input.vin,
                            "/servicescheduler/appointment/",
                            input.id
                        )
                    )
                )
            }
            coVerify {
                communicationManager.get<AppointmentDetailsMaseratiResponse>(
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
    fun `when execute then make a network call with failure response`() {
        val error = PIMSFoundationError.serverError(1, "test_body")

        coEvery {
            communicationManager.get<AppointmentDetailsMaseratiResponse>(any(), any())
        } returns Failure(error)

        runTest {
            val response = executor.execute(input)
            verify {
                val type = object : TypeToken<AppointmentDetailsMaseratiResponse>() {}.type
                executor.request(
                    type = eq(type),
                    urls = eq(
                        arrayOf(
                            "/v1/accounts/",
                            uid,
                            "/vehicles/",
                            input.vin,
                            "/servicescheduler/appointment/",
                            input.id
                        )
                    ),
                    queries = null,
                    body = null
                )
            }

            coVerify {
                communicationManager.get<AppointmentDetailsMaseratiResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }
            Assert.assertEquals(true, response is Failure)
            val failure = response as Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError, failure.error?.subError)
        }
    }

    @Test
    fun `when execute params with the right input then return DealerAppointmentsInput`() {
        val params = mapOf(
            Constants.Input.ID to input.id,
            Constants.Input.VIN to input.vin
        )
        val paramsInput = executor.params(params)
        Assert.assertEquals(input, paramsInput)
    }

    @Test
    fun `when execute params with missing appointmentId then throw missing parameter`() {
        val input = mapOf(Constants.Input.VIN to input.vin)
        val exception = PIMSFoundationError.missingParameter(Constants.Input.ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf(Constants.Input.ID to input.id)
        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
