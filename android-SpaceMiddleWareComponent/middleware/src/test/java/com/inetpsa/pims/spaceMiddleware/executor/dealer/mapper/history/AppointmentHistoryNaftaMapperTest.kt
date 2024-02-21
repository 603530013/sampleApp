package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history

import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryNAFTAResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryNAFTAResponse.Appointment
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test
import java.util.Collections

internal class AppointmentHistoryNaftaMapperTest : FcaExecutorTestHelper() {

    private val mapper: AppointmentHistoryNaftaMapper = spyk()

    @Test
    fun `transformOutput should return HistoryOutput with appointments`() {
        // given
        val appointmentHistoryLatamResponse = AppointmentHistoryNAFTAResponse(
            appointments = listOf(
                Appointment(
                    appointmentId = "serviceIdTest",
                    scheduledTime = 1656414000000,
                    status = AppointmentStatusResponse.Requested
                )
            )
        )

        // when
        val historyOutput = mapper.transformOutput(appointmentHistoryLatamResponse)

        // then
        Assert.assertEquals(
            "HistoryOutput(appointments=[Appointment(appointmentId=serviceIdTest, " +
                "scheduledTime=2022-06-28T11:00Z, status=Requested)])",
            historyOutput.toString()
        )
    }

    @Test
    fun `transformOutput should return HistoryOutput with empty appointments`() {
        // given
        val appointmentHistoryLatamResponse = AppointmentHistoryNAFTAResponse(
            appointments = Collections.emptyList()
        )

        // when
        val historyOutput = mapper.transformOutput(appointmentHistoryLatamResponse)

        // then
        Assert.assertEquals(
            "HistoryOutput(appointments=[])",
            historyOutput.toString()
        )
    }

    @Test
    fun `transformAppointment should return HistoryOutput with appointment`() {
        // given
        val appointmentHistoryLatamResponse = Appointment(
            appointmentId = "serviceIdTest",
            scheduledTime = 1656414000000,
            status = AppointmentStatusResponse.Requested
        )

        // when
        val historyOutput = mapper.transformItem(appointmentHistoryLatamResponse)

        // then
        Assert.assertEquals(
            "Appointment(appointmentId=serviceIdTest, scheduledTime=2022-06-28T11:00Z, status=Requested)",
            historyOutput.toString()
        )
    }

    @Test
    fun `transformAppointment should return HistoryOutput with appointment without scheduledTime`() {
        // given
        val appointmentHistoryLatamResponse = Appointment(
            appointmentId = "serviceIdTest",
            scheduledTime = null,
            status = AppointmentStatusResponse.Requested
        )

        // when
        val historyOutput = mapper.transformItem(appointmentHistoryLatamResponse)

        // then
        Assert.assertNull(historyOutput)
    }

    @Test
    fun `transformScheduledTime should return HistoryOutput Appointment ScheduledTime`() {
        // given
        val time = 1656414000000

        // when
        val result = mapper.transformScheduledTime(time)

        // then
        Assert.assertEquals("2022-06-28T11:00Z", result.toString())
    }

    @Test
    fun `transformScheduledTime should return HistoryOutput Appointment ScheduledTime null`() {
        // given
        val time = null

        // when
        val result = mapper.transformScheduledTime(time)

        // then
        Assert.assertEquals(null, result)
    }

    @Test
    fun `transformScheduledTime should return HistoryOutput Appointment ScheduledTime null with exception`() {
        // given
        val time = -1L

        // when
        val result = mapper.transformScheduledTime(time)

        // then
        Assert.assertEquals("1969-12-31T23:59:59.999Z", result.toString())
    }
}
