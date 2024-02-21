package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Failure
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status.Requested
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsLatamResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsLatamResponse.Customer
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsLatamResponse.Mileage
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsLatamResponse.Services
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsLatamResponse.Services.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryLATAMResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetAppointmentDetailsForLatamExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetAppointmentDetailsForLatamExecutor

    private val input = DetailsInput(
        vin = "testVin",
        id = "testAppointmentId"
    )

    private val detailsOutput = DetailsOutput(
        id = "testAppointmentId",
        vin = "testVin",
        bookingId = "testDealerId",
        bookingLocation = "null",
        scheduledTime = "1970-01-01T00:02:03.456Z",
        comment = "null",
        mileage = "123.0 null",
        email = "testEmail",
        phone = "null",
        status = Requested,
        services = listOf(DetailsOutput.Service(id = "123", name = "testName", price = 123.45f)),
        amount = 123.45f,
        reminders = emptyList()
    )

    private val appointmentDetailsLatamResponse = AppointmentDetailsLatamResponse(
        customer = Customer(email = "testEmail", firstName = "testFirstName", lastName = "testLastName"),
        mileage = Mileage(value = 123.0),
        scheduledTime = 123456,
        services = Services(
            drs = listOf(Service(id = 123, name = "testName", price = 123.45f))
        ),
        status = AppointmentStatusResponse.Requested
    )

    @Before
    override fun setup() {
        mockkObject(BookingOnlineCache)
        super.setup()
        executor = spyk(GetAppointmentDetailsForLatamExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute then make a network API call with success response`() {
        val dealerInput = DetailsInput(
            vin = "testVin",
            id = "testAppointmentId"
        )
        mockkStatic(BookingOnlineCache::class)
        every { BookingOnlineCache.readAppointmentFromLatam(dealerInput.id) } returns AppointmentHistoryLATAMResponse
            .Appointment(dealerId = "testDealerId")

        coEvery {
            communicationManager.get<AppointmentDetailsLatamResponse>(any(), TokenType.AWSToken(FCAApiKey.SDP))
        } returns Success(appointmentDetailsLatamResponse)
        runBlocking {
            val type = object : TypeToken<AppointmentDetailsLatamResponse>() {}.type
            val request = executor.request(
                type = type,
                urls =
                arrayOf(
                    "/v1/accounts/",
                    uid,
                    "/vehicles/",
                    dealerInput.vin,
                    "/servicescheduler/department/",
                    "testDealerId",
                    "/appointment/",
                    dealerInput.id
                ),
                queries = null,
                body = null
            )
            val response = executor.execute(dealerInput)
            coVerify(exactly = 1) { BookingOnlineCache.readAppointmentFromLatam(dealerInput.id) }
            coVerify {
                communicationManager.get<AppointmentDetailsLatamResponse>(
                    request = request,
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
        mockkStatic(BookingOnlineCache::class)
        every { BookingOnlineCache.readAppointmentFromLatam(input.id) } returns AppointmentHistoryLATAMResponse
            .Appointment(dealerId = "testDealerId")
        coEvery {
            communicationManager.get<AppointmentDetailsLatamResponse>(any(), TokenType.AWSToken(FCAApiKey.SDP))
        } returns Failure(error)

        runBlocking {
            val type = object : TypeToken<AppointmentDetailsLatamResponse>() {}.type
            val request = executor.request(
                type = type,
                urls =
                arrayOf(
                    "/v1/accounts/",
                    uid,
                    "/vehicles/",
                    input.vin,
                    "/servicescheduler/department/",
                    "testDealerId",
                    "/appointment/",
                    input.id
                ),
                queries = null,
                body = null
            )
            val response = executor.execute(input)
            coVerify(exactly = 1) { BookingOnlineCache.readAppointmentFromLatam(input.id) }
            coVerify {
                communicationManager.get<AppointmentDetailsLatamResponse>(
                    request = request,
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
    fun `when execute params with the right input then return DetailsInput`() {
        val params = mapOf(
            Constants.Input.ID to input.id,
            Constants.Input.VIN to input.vin,
            Constants.Input.Appointment.BOOKING_ID to "6414"
        )
        val paramsInput = executor.params(params)
        Assert.assertEquals(input, paramsInput)
    }

    @Test
    fun `when execute params with missing appointmentId then throw missing parameter`() {
        val input = mapOf(Constants.Input.VIN to "testVin")
        val exception = PIMSFoundationError.missingParameter(Constants.Input.ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing dealerId then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.ID to input.id,
            Constants.Input.VIN to "testVin"
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
    fun `when read value from cache and dealerId missing then throw missing parameter`() {
        val error = PIMSFoundationError.missingParameter(Appointment.BOOKING_ID)
        mockkStatic(BookingOnlineCache::class)
        every { BookingOnlineCache.readAppointmentFromLatam(input.id)?.dealerId } returns NetworkResponse.Failure(error)
            .toString()
        coEvery {
            communicationManager.get<AppointmentDetailsLatamResponse>(any(), TokenType.AWSToken(FCAApiKey.SDP))
        } returns Failure(error)
        runBlocking {
            val response = executor.execute(input)
            coVerify(exactly = 1) { BookingOnlineCache.readAppointmentFromLatam(input.id) }
            coVerify { communicationManager.get<AppointmentDetailsLatamResponse>(any(), any()) }
            Assert.assertEquals(true, response is Failure)
            val failure = response as Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError, failure.error?.subError)
        }
    }
}
