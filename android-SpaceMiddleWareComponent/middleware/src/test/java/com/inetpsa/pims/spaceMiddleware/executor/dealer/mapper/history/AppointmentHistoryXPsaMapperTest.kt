package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history

import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status.Booked
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status.Cancelled
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status.Closed
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status.Requested
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CachedAppointmentsXPSA
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CachedAppointmentsXPSA.CachedAppointmentXPSA
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput.Appointment
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class AppointmentHistoryXPsaMapperTest : FcaExecutorTestHelper() {

    private val appointmentResponse = CachedAppointmentsXPSA(
        appointments = hashSetOf(
            CachedAppointmentXPSA(
                appointmentId = "1245910",
                status = "Booked",
                date = "2023-10-03 10:30:40"
            )
        )
    )
    private val historyOutput =
        HistoryOutput(
            appointments = listOf(
                Appointment("1245910", "2023-10-03 10:30:40", Booked)
            )
        )

    private val mapper: AppointmentHistoryXPsaMapper = spyk()

    @Test
    fun `when execute and there is a valid cache then return response from cache`() {
        val response = mapper.transformOutput(appointmentResponse)
        Assert.assertEquals(historyOutput, response)
    }

    @Test
    fun `when execute there is a no valid cache then return with failure response`() {
        val response = mapper.transformOutput(CachedAppointmentsXPSA((hashSetOf())))
        Assert.assertEquals(HistoryOutput(listOf()).toString(), response.toString())
    }

    @Test
    fun `transformOutput should return HistoryOutput with appointments`() {
        runBlocking {
            val result = mapper.transformOutput(appointmentResponse)
            Assert.assertEquals(result, historyOutput)
        }
    }

    @Test
    fun `transformStatus should return HistoryOutput Appointment Status null`() {
        // given
        val status = null

        // when
        val result = mapper.transformStatus(status)

        // then
        Assert.assertEquals(null, result)
    }

    @Test
    fun `transformStatus should return HistoryOutput Appointment Status Requested`() {
        val status = Requested
        val result = mapper.transformStatus(status.toString())
        Assert.assertEquals("Requested", result.toString())
    }

    @Test
    fun `transformStatus should return HistoryOutput Appointment Status Booked`() {
        val status = Booked
        val result = mapper.transformStatus(status.toString())

        // then
        Assert.assertEquals("Booked", result.toString())
    }

    @Test
    fun `transformStatus should return HistoryOutput Appointment Status Cancelled`() {
        val status = Cancelled
        val result = mapper.transformStatus(status.toString())
        Assert.assertEquals("Cancelled", result.toString())
    }

    @Test
    fun `transformStatus should return HistoryOutput Appointment Status Closed`() {
        val status = Closed
        val result = mapper.transformStatus(status.toString())
        Assert.assertEquals("Closed", result.toString())
    }

    @Test
    fun `transformStatus should return HistoryOutput Appointment Status  null`() {
        val result = mapper.transformStatus("null")
        Assert.assertEquals(null, result)
    }
}
