package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.agenda

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.department.GetNaftaDepartmentIdExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token.NaftaTokenManager
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput.TimeFence.MONTH
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaNAFTAResponse
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
import java.time.LocalDate

internal class GetAgendaForNaftaFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetAgendaForNaftaFcaExecutor
    private val token = "testToken"
    private val departmentId = 4567
    private val startDate = LocalDate.of(2023, 7, 20).toString()

    val response = DealerAgendaNAFTAResponse(listOf())

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(NaftaTokenManager::class)
        mockkConstructor(GetNaftaDepartmentIdExecutor::class)
        every { anyConstructed<NaftaTokenManager>().getOssTokenFromCache(any()) } returns token
        every { anyConstructed<NaftaTokenManager>().isTokenUnauthorized<Unit>(any()) } returns false
        coEvery { anyConstructed<GetNaftaDepartmentIdExecutor>().execute(any()) } returns Success(departmentId)
        coEvery { anyConstructed<GetNaftaDepartmentIdExecutor>().execute(any(), any()) } returns Success(departmentId)

        mockkConstructor(NaftaTokenManager::class)
        every { anyConstructed<NaftaTokenManager>().getOssTokenFromCache(any()) } returns "testToken"
        executor = spyk(GetAgendaForNaftaFcaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute with missing start then throw missing params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "7019500",
            Constants.Input.Appointment.BOOKING_LOCATION to "testLocation",
            Constants.Input.Appointment.TIME_FENCE to "MONTH",
            Constants.Input.VIN to "testVin",
            Constants.Input.Appointment.DEPARTMENT_ID to 12345
        )

        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.START_DATE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with invalid start then throw invalid params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "7019500",
            Constants.Input.Appointment.BOOKING_LOCATION to "testLocation",
            Constants.Input.Appointment.START_DATE to 208980,
            Constants.Input.Appointment.TIME_FENCE to "MONTH",
            Constants.Input.VIN to "testVin",
            Constants.Input.Appointment.DEPARTMENT_ID to 12345
        )

        val exception = PIMSFoundationError.invalidParameter(Constants.Input.Appointment.START_DATE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with missing timeFence then throw missing params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "7019500",
            Constants.Input.Appointment.BOOKING_LOCATION to "testLocation",
            Constants.Input.Appointment.START_DATE to startDate,
            Constants.Input.VIN to "testVin",
            Constants.Input.Appointment.DEPARTMENT_ID to 12345
        )

        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.TIME_FENCE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with invalid timeFence then throw invalid params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "7019500",
            Constants.Input.Appointment.BOOKING_LOCATION to "testLocation",
            Constants.Input.Appointment.START_DATE to startDate,
            Constants.Input.Appointment.TIME_FENCE to "DAY",
            Constants.Input.VIN to "testVin",
            Constants.Input.Appointment.DEPARTMENT_ID to 12345
        )

        val exception = PIMSFoundationError.invalidParameter(Constants.Input.Appointment.TIME_FENCE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with missing departmentId then throw missing params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "7019500",
            Constants.Input.Appointment.BOOKING_LOCATION to "testLocation",
            Constants.Input.Appointment.START_DATE to startDate,
            Constants.Input.Appointment.TIME_FENCE to "MONTH",
            Constants.Input.VIN to "testVin"
        )

        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.DEPARTMENT_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with invalid departmentId then throw invalid params`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "7019500",
            Constants.Input.Appointment.BOOKING_LOCATION to "testLocation",
            Constants.Input.Appointment.START_DATE to startDate,
            Constants.Input.Appointment.TIME_FENCE to "MONTH",
            Constants.Input.VIN to "testVin",
            Constants.Input.Appointment.DEPARTMENT_ID to 12345
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
    fun `when execute then make a get API call`() {
        val input = AgendaInput(
            dealerId = "7019500|0",
            startDate = LocalDate.of(2023, 7, 20),
            timeFence = MONTH,
            vin = "testVin",
            serviceIds = null
        )

        every { executor.params(any()) } returns input

        coEvery { communicationManager.get<DealerAgendaNAFTAResponse>(any(), any()) } returns Success(
            DealerAgendaNAFTAResponse(null)
        )

        runTest {
            executor.execute(input, token = token)

            verify {
                executor.request(
                    type = eq(DealerAgendaNAFTAResponse::class.java),
                    urls = eq(
                        arrayOf(
                            "/v1/servicescheduler/department/",
                            "$departmentId",
                            "/timesegments"
                        )
                    ),
                    queries = any(),
                    headers = eq(mapOf("dealer-authorization" to token))
                )
            }

            coVerify {
                communicationManager.get<DealerAgendaNAFTAResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }
        }
    }
}
