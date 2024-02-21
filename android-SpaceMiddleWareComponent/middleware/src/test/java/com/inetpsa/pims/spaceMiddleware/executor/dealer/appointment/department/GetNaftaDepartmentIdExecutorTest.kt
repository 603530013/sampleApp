package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.department

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token.NaftaTokenManager
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.DepartmentIdInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesBodyRequest
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DepartmentIDResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetNaftaDepartmentIdExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetNaftaDepartmentIdExecutor
    private val token = "testToken"
    private val departmentId = 7658

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(NaftaTokenManager::class)
        every { anyConstructed<NaftaTokenManager>().getOssTokenFromCache(any()) } returns "testToken"

        coEvery { communicationManager.post<DepartmentIDResponse>(any(), any()) } returns Success(
            DepartmentIDResponse(departmentId)
        )

        executor = spyk(GetNaftaDepartmentIdExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute with already cached value then return the id from cache`() {
        val input = DepartmentIdInput(
            dealerId = "7019500",
            services = emptyList()
        )
        BookingOnlineCache.write(input, departmentId)

        every { executor.params(any()) } returns input

        runTest {
            val response = executor.execute()

            verify(exactly = 0) {
                executor.request(
                    type = eq(DepartmentIDResponse::class.java),
                    urls = eq(arrayOf("/v1/servicescheduler/dealerdepartment")),
                    body = ServicesBodyRequest(emptyList()).toJson(),
                    headers = eq(mapOf("dealer-authorization" to token))
                )
            }

            coVerify(exactly = 0) {
                communicationManager.post<DepartmentIDResponse>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(departmentId, success.response)
        }
    }

    @Test
    fun `when execute with not cached value then make an API call`() {
        val input = DepartmentIdInput(
            dealerId = "7019500",
            services = emptyList()
        )

        every { executor.params(any()) } returns input

        runTest {
            BookingOnlineCache.clear()
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(DepartmentIDResponse::class.java),
                    urls = eq(arrayOf("/v1/servicescheduler/dealerdepartment")),
                    body = ServicesBodyRequest(emptyList()).toJson(),
                    headers = eq(mapOf("dealer-authorization" to token))
                )
            }

            coVerify(exactly = 1) {
                communicationManager.post<DepartmentIDResponse>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(departmentId, success.response)
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
    fun `when execute with missing services then return valid input`() {
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
    fun `when execute with services blank then throw missing params`() {
        val input = mapOf(
            Appointment.BOOKING_ID to "20230720",
            Appointment.SERVICES to emptyList<String>()
        )

        val exception = PIMSFoundationError.missingParameter(Appointment.SERVICES)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with invalid services then throw invalid params`() {
        val input = mapOf(
            Appointment.BOOKING_ID to "20230720",
            Appointment.SERVICES to 20230720
        )
        val exception = PIMSFoundationError.invalidParameter(Appointment.SERVICES)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `test params() returns DepartmentIDInput`() {
        val testInputParams = mapOf(
            Appointment.BOOKING_ID to "7019500",
            Appointment.BOOKING_LOCATION to "testLocation",
            Appointment.SERVICES to emptyList<String>()
        )
        val expectedInputParams = DepartmentIdInput(
            dealerId = "7019500",
            services = emptyList()
        )
        val executor = GetNaftaDepartmentIdExecutor(middlewareComponent, testInputParams)
        Assert.assertEquals(expectedInputParams, executor.params())
    }

    @Test
    fun `test params() throws PIMSError when booking id is not present`() {
        val executor = GetNaftaDepartmentIdExecutor(middlewareComponent)
        Assert.assertThrows(PIMSError::class.java) {
            executor.params()
        }
    }
}
