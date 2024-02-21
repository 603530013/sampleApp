package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history.AppointmentHistoryXPsaMapper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status.Booked
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CachedAppointmentsXPSA
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput.Appointment
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetAppointmentListPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetAppointmentListPsaExecutor

    private val appointmentResponse = CachedAppointmentsXPSA(
        appointments = hashSetOf(
            CachedAppointmentsXPSA.CachedAppointmentXPSA(
                appointmentId = "1245910",
                status = "Booked",
                date = "2023-10-03 10:30:40"
            )
        )
    )

    private val historyOutput = HistoryOutput(
        appointments = listOf(
            Appointment("1245910", "2023-10-03 10:30:40", Booked)
        )
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(AppointmentHistoryXPsaMapper::class)
        every { anyConstructed<AppointmentHistoryXPsaMapper>().transformOutput(any()) } returns historyOutput
        every { middlewareComponent.dataManager } returns dataManager
        executor = spyk(GetAppointmentListPsaExecutor(baseCommand), recordPrivateCalls = true)
    }

    @Test
    fun `when execute params with empty input then continue execution`() {
        val output = executor.params(null)
        Assert.assertEquals(Unit, output)
    }

    @Test
    fun `when execute readFromCache in available case then return cache`() {
        every { dataManager.read(any(), any()) } returns appointmentResponse.toJson()
        val cache = executor.readFromCache()
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_appointment_xPSA"), eq(APPLICATION)) }
        Assert.assertEquals(appointmentResponse, cache)
    }

    @Test
    fun `when execute readFromCache in empty case then return empty list`() {
        every { dataManager.read(any(), any()) } returns "   "
        val cache = executor.readFromCache()
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_appointment_xPSA"), eq(APPLICATION)) }
        Assert.assertEquals(CachedAppointmentsXPSA(HashSet()).toJson(), cache.toJson())
    }

    @Test
    fun `when execute then return transformed response`() {
        every { executor.readFromCache() } returns appointmentResponse
        runTest {
            val response = executor.execute()
            verify(exactly = 1) { executor.readFromCache() }
            verify(exactly = 1) { anyConstructed<AppointmentHistoryXPsaMapper>().transformOutput(appointmentResponse) }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(historyOutput.toJson(), success.response.toJson())
        }
    }
}
