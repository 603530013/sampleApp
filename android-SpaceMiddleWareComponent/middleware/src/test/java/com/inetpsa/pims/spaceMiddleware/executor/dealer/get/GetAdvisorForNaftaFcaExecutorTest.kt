package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.department.GetNaftaDepartmentIdExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token.NaftaTokenManager
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAdvisorResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAdvisorResponse.ServiceAdvisors
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetAdvisorForNaftaFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetAdvisorForNaftaFcaExecutor

    private val token = "testToken"
    private val departmentId = 5445

    private val advisorInput = OssTokenInput(
        dealerId = "1234567"
    )
    private val advisorResponse = DealerAdvisorResponse(
        advisors = listOf(
            ServiceAdvisors(
                id = 1024,
                name = "testAdvisorName",
                memberId = 124
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        mockkObject(BookingOnlineCache)

        mockkConstructor(NaftaTokenManager::class)
        mockkConstructor(GetNaftaDepartmentIdExecutor::class)
        every { anyConstructed<NaftaTokenManager>().getOssTokenFromCache(any()) } returns token
        every { anyConstructed<NaftaTokenManager>().isTokenUnauthorized<Unit>(any()) } returns false
        coEvery { anyConstructed<GetNaftaDepartmentIdExecutor>().execute(any()) } returns Success(departmentId)
        coEvery { anyConstructed<GetNaftaDepartmentIdExecutor>().execute(any(), any()) } returns Success(departmentId)

        mockkConstructor(NaftaTokenManager::class)
        every { anyConstructed<NaftaTokenManager>().getOssTokenFromCache(any()) } returns "testToken"

        executor = spyk(GetAdvisorForNaftaFcaExecutor(middlewareComponent, mapOf()))
    }

    @Test
    fun `when execute params with missing departmentId then throw invalid parameter`() {
        val departmentId = "abcd"
        val dealerId = "1234567"
        val dealerLocation = "testLocation"

        val input = mapOf(
            Constants.Input.Appointment.DEPARTMENT_ID to departmentId,
            Constants.Input.Appointment.BOOKING_ID to dealerId,
            Constants.Input.Appointment.BOOKING_LOCATION to dealerLocation
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.Appointment.DEPARTMENT_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with empty params then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with right inputs then return an DealerAppointmentInput`() {
        val departmentId = 1024
        val dealerId = "1234567"
        val dealerLocation = "testLocation"
        val input = mapOf(
            Constants.Input.Appointment.DEPARTMENT_ID to departmentId,
            Constants.Input.Appointment.BOOKING_ID to dealerId,
            Constants.Input.Appointment.BOOKING_LOCATION to dealerLocation
        )

        val dealerAppointmentInput = executor.params(input)
        Assert.assertEquals(advisorInput, dealerAppointmentInput)
    }

    @Test
    fun `when execute then make a network API call with success response`() {
        val token = "testToken"

        every { executor.params(any()) } returns advisorInput
        coEvery {
            communicationManager.get<DealerAdvisorResponse>(any(), any())
        } returns NetworkResponse.Success(advisorResponse)

        coJustRun { BookingOnlineCache.write(any<DealerAdvisorResponse>()) }
        runBlocking {
            val response = executor.execute(advisorInput, token)
            verify {
                executor.request(
                    type = eq(DealerAdvisorResponse::class.java),
                    urls = eq(
                        arrayOf(
                            "/v1/servicescheduler/department/",
                            "$departmentId",
                            "/advisors"
                        )
                    ),
                    headers = mapOf("dealer-authorization" to token)
                )
            }
            coVerify {
                communicationManager.get<DealerAdvisorResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }
            coVerify(exactly = 1) { BookingOnlineCache.write(advisorResponse) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(advisorResponse, success.response)
        }
    }

    @Test
    fun `when execute then make a network API call with Failure response`() {
        val token = "testToken"
        every { executor.params(any()) } returns advisorInput
        val error = PimsErrors.serverError(null, "testError")
        coEvery { communicationManager.get<DealerAdvisorResponse>(any(), any()) } returns NetworkResponse.Failure(error)
        runBlocking {
            val response = executor.execute(advisorInput, token)
            verify {
                executor.request(
                    type = eq(DealerAdvisorResponse::class.java),
                    urls = eq(
                        arrayOf(
                            "/v1/servicescheduler/department/",
                            "$departmentId",
                            "/advisors"
                        )
                    ),
                    headers = mapOf("dealer-authorization" to token)
                )
            }
            coVerify {
                communicationManager.get<DealerAdvisorResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }
            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = (response as NetworkResponse.Failure).error
            Assert.assertEquals(error, failure)
        }
    }
}
