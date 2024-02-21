package com.inetpsa.pims.spaceMiddleware.executor.dealer.set

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.mmx.foundation.tools.Market.LATAM
import com.inetpsa.mmx.foundation.tools.Market.NAFTA
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.set.SetEmeaDeleteAppointmentFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.set.SetLatamDeleteAppointmentFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.set.SetNaftaDeleteAppointmentFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DeleteDealerAppointmentResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class DeleteAppointmentsFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: DeleteAppointmentsFcaExecutor

    private val input = mapOf(
        Input.VIN to "testVin",
        Input.ID to "testAppointmentId",
        Input.Appointment.BOOKING_ID to "testDealerBookingId",
        Input.Appointment.BOOKING_LOCATION to "testDepartmentId"
    )
    private val response = DeleteDealerAppointmentResponse(
        "testDeleteId",
        "testDeleteDescription"
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(SetEmeaDeleteAppointmentFcaExecutor::class)
        mockkConstructor(SetLatamDeleteAppointmentFcaExecutor::class)
        mockkConstructor(SetNaftaDeleteAppointmentFcaExecutor::class)
        coEvery { anyConstructed<SetEmeaDeleteAppointmentFcaExecutor>().execute(any()) } returns Success(Unit)
        coEvery { anyConstructed<SetLatamDeleteAppointmentFcaExecutor>().execute(any()) } returns Success(Unit)
        coEvery { anyConstructed<SetNaftaDeleteAppointmentFcaExecutor>().execute(any()) } returns Success(Unit)

        executor = spyk(DeleteAppointmentsFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute params with the right input then return DeleteAppointmentInput`() {
        val output = executor.params(input)
        Assert.assertEquals(input, output)
    }

    @Test
    fun `when market is EMEA execute then make a network call with success response`() =
        runTest {
            every { executor.params(any()) } returns input
            every { configurationManager.market } returns EMEA
            coEvery { communicationManager.delete<DeleteDealerAppointmentResponse>(any(), any()) } returns Success(
                response
            )

            executor.execute(input)
            coVerify { anyConstructed<SetEmeaDeleteAppointmentFcaExecutor>().execute(any()) }
        }

    @Test
    fun `when market is LATAM execute then make a network call with success response`() =
        runTest {
            every { executor.params(any()) } returns input
            every { configurationManager.market } returns LATAM
            coEvery { communicationManager.delete<DeleteDealerAppointmentResponse>(any(), any()) } returns Success(
                response
            )

            executor.execute(input)
            coVerify { anyConstructed<SetLatamDeleteAppointmentFcaExecutor>().execute(any()) }
        }

    @Test
    fun `when market is NAFTA execute then make a network call with success response`() =
        runTest {
            every { executor.params(any()) } returns input
            every { configurationManager.market } returns NAFTA
            coEvery { communicationManager.delete<DeleteDealerAppointmentResponse>(any(), any()) } returns Success(
                response
            )

            executor.execute(input)
            coVerify { anyConstructed<SetNaftaDeleteAppointmentFcaExecutor>().execute(any()) }
        }
}
