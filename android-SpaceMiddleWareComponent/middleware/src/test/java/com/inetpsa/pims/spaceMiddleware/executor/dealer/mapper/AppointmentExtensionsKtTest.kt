package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper

import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse
import org.junit.Assert
import org.junit.Test

class AppointmentExtensionsKtTest {

    @Test
    fun `transformStatus should return HistoryOutput Appointment Status null`() {
        // given
        val status = null

        // when
        val result = status.transformStatus()

        // then
        Assert.assertEquals(null, result)
    }

    @Test
    fun `transformStatus should return HistoryOutput Appointment Status Requested`() {
        // given
        val status = AppointmentStatusResponse.Requested

        // when
        val result = status.transformStatus()

        // then
        Assert.assertEquals("Requested", result.toString())
    }

    @Test
    fun `transformStatus should return HistoryOutput Appointment Status Booked`() {
        // given
        val status = AppointmentStatusResponse.Booked

        // when
        val result = status.transformStatus()

        // then
        Assert.assertEquals("Booked", result.toString())
    }

    @Test
    fun `transformStatus should return HistoryOutput Appointment Status Cancelled`() {
        // given
        val status = AppointmentStatusResponse.Cancelled

        // when
        val result = status.transformStatus()

        // then
        Assert.assertEquals("Cancelled", result.toString())
    }

    @Test
    fun `transformStatus should return HistoryOutput Appointment Status Closed`() {
        // given
        val status = AppointmentStatusResponse.Closed

        // when
        val result = status.transformStatus()

        // then
        Assert.assertEquals("Closed", result.toString())
    }
}
