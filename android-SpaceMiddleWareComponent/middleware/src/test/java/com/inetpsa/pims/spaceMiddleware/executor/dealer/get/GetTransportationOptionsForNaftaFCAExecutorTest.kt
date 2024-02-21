package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.department.GetNaftaDepartmentIdExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.DepartmentIdInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TransportationOptionsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TransportationOptionsResponse.TransportationOption
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class GetTransportationOptionsForNaftaFCAExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetTransportationOptionsForNaftaFCAExecutor

    private val testResponse = TransportationOptionsResponse(
        transportations = listOf(
            TransportationOption("own-ride", "Own ride"),
            TransportationOption("need-shuttle", "Shuttle ride")
        )
    )

    private val departmentIdInput = DepartmentIdInput("1644", null)

    @Before
    fun setUp() {
        mockkObject(BookingOnlineCache)
        executor = spyk(GetTransportationOptionsForNaftaFCAExecutor(baseCommand))
    }

    @Test
    fun `test params()`() {
        val params = executor.params(mapOf(Appointment.BOOKING_ID to "6444"))
        assertEquals(DepartmentIdInput("6444", null), params)
    }

    @Test
    fun `test execute() saves success response on cache`() {
        mockkConstructor(GetNaftaDepartmentIdExecutor::class)
        coEvery {
            anyConstructed<GetNaftaDepartmentIdExecutor>().execute(departmentIdInput)
        } returns Success(5445)

        coEvery {
            communicationManager.get<TransportationOptionsResponse>(
                any(),
                TokenType.AWSToken(FCAApiKey.SDP)
            )
        } returns Success(testResponse)
        coJustRun { BookingOnlineCache.write(any<TransportationOptionsResponse>()) }
        runBlocking {
            val response = executor.execute(departmentIdInput, "")
            val result = (response as Success).response
            coVerify { BookingOnlineCache.write(result) }
            assertEquals(testResponse, result)
            assertEquals(testResponse.transportations, result.transportations)
            assertEquals(
                testResponse.transportations?.first()?.code,
                result.transportations?.first()?.code
            )
            assertEquals(
                testResponse.transportations?.first()?.description,
                result.transportations?.first()?.description
            )
        }
    }
}
