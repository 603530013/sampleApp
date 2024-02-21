package com.inetpsa.pims.spaceMiddleware.executor.dealer.set

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CreateEMEAInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CreateOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AddDealerAppointmentResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset

internal class AddDealerAppointmentFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: AddDealerAppointmentFcaExecutor

    private val date = LocalDateTime.of(2023, Month.OCTOBER, 3, 10, 30, 40, 50000)

    private val createInput = CreateEMEAInput(
        vin = "testVin",
        date = date,
        mileage = 1073,
        codNation = "testCodNation",
        comment = "testFaultDescription",
        bookingId = "testId",
        bookingLocation = "testLocation",
        contactName = "testContactName",
        contactPhone = "testTelephone",
        services = listOf()
    )

    private val response = AddDealerAppointmentResponse(
        codRepairOrder = "testId",
        description = "testDescription"
    )

    private val createOutput = CreateOutput(
        id = "testId"
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(AddDealerAppointmentFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = emptyMap<String, String>()

        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with right inputs then return an DealerAppointmentInput`() {
        val input = mapOf(
            Constants.Input.VIN to createInput.vin,
            Constants.Input.DATE to date.toString(),
            Constants.PARAM_MILEAGE to createInput.mileage,
            Constants.Input.COD_NATION to createInput.codNation,
            Constants.BODY_PARAM_COMMENT to createInput.comment,
            Constants.Input.Appointment.BOOKING_ID to createInput.bookingId,
            Constants.Input.Appointment.BOOKING_LOCATION to createInput.bookingLocation,
            Constants.Input.CONTACT_NAME to createInput.contactName,
            Constants.Input.CONTACT_PHONE to createInput.contactPhone,
            Constants.Input.PARAM_SERVICES to createInput.services
        )
        val result = executor.params(input)
        assertEquals(createInput, result)
    }

    @Test
    fun `when execute params with missing vehicleKm then throw missing parameter`() {
        val input = mapOf(Constants.Input.VIN to "testVin", Constants.Input.DATE to date.toString())
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_MILEAGE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing codNation then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.VIN to "testVin",
            Constants.Input.DATE to date.toString(),
            Constants.PARAM_MILEAGE to 1073
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.COD_NATION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing faultDescription then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.VIN to "testVin",
            Constants.Input.DATE to date.toString(),
            Constants.PARAM_MILEAGE to 1073,
            Constants.Input.COD_NATION to "testCodNation"
        )
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_COMMENT)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing id then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.VIN to "testVin",
            Constants.Input.DATE to date.toString(),
            Constants.PARAM_MILEAGE to 1073,
            Constants.Input.COD_NATION to "testCodNation",
            Constants.BODY_PARAM_COMMENT to "testFaultDescription"
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing telephone then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.VIN to "testVin",
            Constants.Input.DATE to date.toString(),
            Constants.PARAM_MILEAGE to 1073,
            Constants.Input.COD_NATION to "testCodNation",
            Constants.BODY_PARAM_COMMENT to "testFaultDescription",
            Constants.Input.Appointment.BOOKING_ID to "testId",
            Constants.Input.Appointment.BOOKING_LOCATION to "testLocation",
            Constants.Input.CONTACT_NAME to "testContactName"
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.CONTACT_PHONE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute then make a network API call with success response`() {
        every { executor.params(any()) } returns createInput
        coEvery { communicationManager.post<AddDealerAppointmentResponse>(any(), any()) } returns Success(response)
        runTest {
            val response = executor.execute()
            val bodyJson = mapOf(
                Constants.PARAMS_KEY_DATE to date.toInstant(ZoneOffset.UTC).toEpochMilli().toString(),
                Constants.PARAMS_KEY_VEHICLE_KM to createInput.mileage,
                Constants.PARAMS_KEY_COD_NATION to createInput.codNation,
                Constants.PARAMS_KEY_FAULT_DESCRIPTION to createInput.comment,
                Constants.PARAMS_KEY_DEALER_ID to createInput.bookingId,
                Constants.PARAMS_KEY_LOCATION to createInput.bookingLocation,
                Constants.PARAMS_KEY_CONTACT_NAME to createInput.contactName,
                Constants.PARAMS_KEY_TELEPHONE to createInput.contactPhone,
                Constants.PARAMS_KEY_SERVICES_LIST to createInput.services

            )
            verify {
                println(bodyJson.toJson())
                executor.request(
                    type = AddDealerAppointmentResponse::class.java,
                    urls = arrayOf(
                        "/v1/accounts/",
                        "testCustomerId",
                        "/vehicles/",
                        "testVin",
                        "/servicescheduler/appointment"
                    ),
                    body = bodyJson.toJson()
                )
            }
            coVerify {
                communicationManager.post<AddDealerAppointmentResponse>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            assertEquals(true, response is Success)
            val success = (response as Success).response
            assertEquals(createOutput, success)
        }
    }
}
