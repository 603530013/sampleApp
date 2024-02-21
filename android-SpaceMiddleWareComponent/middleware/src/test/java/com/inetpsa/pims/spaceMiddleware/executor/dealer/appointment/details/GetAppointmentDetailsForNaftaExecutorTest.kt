package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Failure
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status.Requested
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsNaftaInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsNaftaResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsNaftaResponse.Advisor
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsNaftaResponse.Customer
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsNaftaResponse.Mileage
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsNaftaResponse.Services
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsNaftaResponse.Services.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsNaftaResponse.TransportationOption
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryNAFTAResponse
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

internal class GetAppointmentDetailsForNaftaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetAppointmentDetailsForNaftaExecutor
    private val token = "testToken"

    private val input = DetailsNaftaInput(
        id = "testAppointmentId",
        dealerId = "testDealerId",
        vin = "testVin"
    )

    private val appointmentDetailsNaftaResponse = AppointmentDetailsNaftaResponse(
        advisor = Advisor(departmentId = 1234, id = 123, name = "testName"),
        customer = Customer(firstName = "testFirstName", id = "testId", lastName = "testLastName"),
        customerConcernsInfo = "testCustomerConcernsInfo",
        mileage = Mileage(unitsKind = "testUnitsKind", value = 123f),
        scheduledTime = 123456,
        services = Services(
            drs = listOf(Service(id = 123, name = "testName", price = 123.45f))
        ),
        status = AppointmentStatusResponse.Requested,
        transportationOption = TransportationOption(code = "testCode", description = "testDescription")
    )

    private val detailsOutput = DetailsOutput(
        id = "testAppointmentId",
        vin = "testVin",
        bookingId = "testDealerId",
        bookingLocation = "null",
        scheduledTime = "1970-01-01T00:02:03.456Z",
        comment = "testCustomerConcernsInfo",
        mileage = "123.0 testUnitsKind",
        email = "null",
        phone = "null",
        status = Requested,
        services = listOf(DetailsOutput.Service(id = "123", name = "testName", price = 123.45f)),
        amount = null,
        reminders = null
    )

    @Before
    override fun setup() {
        super.setup()
        mockkObject(BookingOnlineCache)
        executor = spyk(GetAppointmentDetailsForNaftaExecutor(middlewareComponent))
    }

    @Test
    fun `when execute then make a network API call with success response`() {
        val dealerInput = DetailsNaftaInput(
            id = "testAppointmentId",
            dealerId = "testDealerId",
            vin = "testVin"
        )
        every { executor.params(any()) } returns dealerInput
        coEvery {
            communicationManager.get<AppointmentDetailsNaftaResponse>(
                any(),
                TokenType.AWSToken(FCAApiKey.SDP)
            )
        } returns Success(appointmentDetailsNaftaResponse)

        runBlocking {
            val response = executor.execute(dealerInput, token = token, departmentId = "7658")
            val request = executor.request(
                type = AppointmentDetailsNaftaResponse::class.java,
                urls = arrayOf(
                    "/v1/accounts/",
                    uid,
                    "/vehicles/",
                    dealerInput.vin,
                    "/servicescheduler/department/",
                    "7658",
                    "/appointment/",
                    dealerInput.id
                ),
                headers = mapOf("dealer-authorization" to token),
                queries = null
            )
            coVerify {
                communicationManager.get<AppointmentDetailsNaftaResponse>(
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
        coEvery {
            communicationManager.get<AppointmentDetailsNaftaResponse>(any(), any())
        } returns Failure(error)
        runBlocking {
            val type = object : TypeToken<AppointmentDetailsNaftaResponse>() {}.type
            val request = executor.request(
                type = type,
                urls = arrayOf(
                    "/v1/accounts/",
                    uid,
                    "/vehicles/",
                    input.vin,
                    "/servicescheduler/department/",
                    "7658",
                    "/appointment/",
                    input.id
                ),
                headers = mapOf("dealer-authorization" to token)
            )
            val response = executor.execute(input, token = token, departmentId = "7658")
            coVerify {
                communicationManager.get<AppointmentDetailsNaftaResponse>(
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
    fun `when execute params with the right input then return DetailsNaftaInput`() {
        mockkStatic(BookingOnlineCache::class)
        every { BookingOnlineCache.readAppointmentFromNafta(input.id) } returns AppointmentHistoryNAFTAResponse
            .Appointment(dealerId = input.dealerId)
        val params = mapOf(
            Constants.Input.VIN to input.vin,
            Constants.Input.ID to input.id,
            Constants.Input.Appointment.BOOKING_ID to input.dealerId
        )
        val paramsInput = executor.params(params)
        Assert.assertEquals(input, paramsInput)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf(Constants.Input.Appointment.DEPARTMENT_ID to "testDepartmentId")
        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)
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
            Constants.Input.VIN to input.vin,
            Constants.Input.ID to input.id,
            Constants.Input.Appointment.DEPARTMENT_ID to "7658",
            Constants.Input.Appointment.BOOKING_LOCATION to "000"
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
    fun `when execute params with missing appointmentId then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.VIN to input.vin,
            Constants.Input.Appointment.BOOKING_ID to input.dealerId,
            Constants.Input.Appointment.DEPARTMENT_ID to "7658",
            Constants.Input.Appointment.BOOKING_LOCATION to "000"
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
