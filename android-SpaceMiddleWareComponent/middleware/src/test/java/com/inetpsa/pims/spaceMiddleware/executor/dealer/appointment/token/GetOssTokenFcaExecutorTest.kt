package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.ServiceSchedulerToken
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

internal class GetOssTokenFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetOssTokenFcaExecutor

    override fun setup() {
        super.setup()
        mockkConstructor(NaftaTokenManager::class)
        executor = spyk(GetOssTokenFcaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute then make a get API call`() {
        every { executor.params(any()) } returns OssTokenInput("8356")

        coEvery { communicationManager.get<ServiceSchedulerToken>(any(), any()) } returns Success(
            ServiceSchedulerToken(accessToken = "7658", expiresIn = 68, tokenType = "tokenType")
        )

        coJustRun { anyConstructed<NaftaTokenManager>().updateToken(any(), any()) }

        runTest {
            executor.execute()

            verify {
                executor.request(
                    type = eq(ServiceSchedulerToken::class.java),
                    urls = eq(arrayOf("/v1/servicescheduler/token")),
                    queries = eq(mapOf(Constants.QUERY_PARAM_KEY_HINT_DEALER to "8356"))
                )
            }

            coVerify {
                communicationManager.get<ServiceSchedulerToken>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }
        }
    }

    @Test
    fun `when execute with missing dealerId then throw missing params`() {
        val input = mapOf<String, Any>()

        val exception = PIMSFoundationError.missingParameter(Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with dealerId blank then throw missing params`() {
        val input = mapOf(Appointment.BOOKING_ID to "")

        val exception = PIMSFoundationError.missingParameter(Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with invalid dealerId then throw invalid params`() {
        val input = mapOf(Appointment.BOOKING_ID to 20230720)
        val exception = PIMSFoundationError.invalidParameter(Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with missing dealerLocation then return valid input`() {
        val input = mapOf(Appointment.BOOKING_ID to "20230720")

        val exception = PIMSFoundationError.missingParameter(Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with dealerLocation blank then throw missing params`() {
        val input = mapOf(
            Appointment.BOOKING_ID to "20230720",
            Appointment.BOOKING_LOCATION to ""
        )

        val exception = PIMSFoundationError.missingParameter(Appointment.BOOKING_LOCATION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `test params() returns OssTokenInput`() {
        val testInputParams = mapOf(
            Appointment.BOOKING_ID to "7019500",
            Appointment.BOOKING_LOCATION to "testLocation"
        )
        val expectedInputParams = OssTokenInput(
            dealerId = "7019500"
        )
        val executor = GetOssTokenFcaExecutor(middlewareComponent, testInputParams)
        Assert.assertEquals(expectedInputParams, executor.params())
    }

    @Test
    fun `test params() throws PIMSError when params are null`() {
        val executor = GetOssTokenFcaExecutor(middlewareComponent)
        Assert.assertThrows(PIMSError::class.java) {
            executor.params()
        }
    }

    @Test
    fun `when execute with invalid dealerLocation then throw invalid params`() {
        val input = mapOf(
            Appointment.BOOKING_ID to "20230720",
            Appointment.BOOKING_LOCATION to 20230720
        )
        val exception = PIMSFoundationError.invalidParameter(Appointment.BOOKING_LOCATION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `test params() throws bookingID missing error when booking id is not present`() {
        val pimsError = PIMSFoundationError.missingParameter(Appointment.BOOKING_ID)
        val testInputParams = mapOf(
            Appointment.BOOKING_LOCATION to "testLocation"
        )
        val executor = GetOssTokenFcaExecutor(middlewareComponent, testInputParams)
        try {
            executor.params()
        } catch (error: PIMSError) {
            Assert.assertEquals(pimsError.message, error.message)
            Assert.assertEquals(pimsError.code, error.code)
        }
    }
}
