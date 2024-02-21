package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.set

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.delete.DeleteEmeaMaseratiInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DeleteDealerAppointmentResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class SetEmeaDeleteAppointmentFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: SetEmeaDeleteAppointmentFcaExecutor
    private val input = DeleteEmeaMaseratiInput(
        vin = "testVin",
        id = "testAppointmentId"
    )

    private val response = DeleteDealerAppointmentResponse(
        "testDeleteId",
        "testDeleteDescription"
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(SetEmeaDeleteAppointmentFcaExecutor(middlewareComponent))
    }

    @Test
    fun `when execute then make a get API call`() {
        every { executor.params(any()) } returns input
        coEvery {
            communicationManager.delete<DeleteDealerAppointmentResponse>(any(), any())
        } returns Success(response)
        runTest {
            val response = executor.execute()
            verify {
                executor.request(
                    type = DeleteDealerAppointmentResponse::class.java,
                    urls = arrayOf(
                        "/v1/accounts/",
                        uid,
                        "/vehicles/",
                        input.vin,
                        "/servicescheduler/appointment/",
                        input.id
                    )
                )
            }

            coVerify {
                communicationManager.delete<DeleteDealerAppointmentResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute params with the right input then return DeleteAppointmentInput`() {
        val params = mapOf(
            Constants.Input.VIN to input.vin,
            Constants.Input.ID to input.id
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

    @Test
    fun `when execute params with missing bookingId then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.ID to input.id,
            Constants.Input.VIN to input.vin
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
    fun `when execute params with missing dealerLocation then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.ID to input.id,
            Constants.Input.VIN to input.vin
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.BOOKING_LOCATION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
