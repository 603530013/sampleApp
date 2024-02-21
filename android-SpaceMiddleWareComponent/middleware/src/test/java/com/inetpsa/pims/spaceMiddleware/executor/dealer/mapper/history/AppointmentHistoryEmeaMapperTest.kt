package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history

import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Reminder
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Reminder.MIN30
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsEmeaResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsEmeaResponse.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryEMEAResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryEMEAResponse.Appointment
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Collections

internal class AppointmentHistoryEmeaMapperTest : FcaExecutorTestHelper() {

    private val mapper: AppointmentHistoryEmeaMapper = spyk()

    private val detailsResponse = AppointmentDetailsEmeaResponse(
        vin = "testVin",
        serviceId = "testServiceId",
        dealerId = "testDealerId",
        location = "testLocation",
        scheduledTime = "2022-06-28T11:30:0000Z",
        faultDescription = "testFaultDescription",
        servicesList = listOf(Service("testServiceList")),
        telephone = "testTelephone",
        vehicleKm = "200000",
        reminders = listOf("1656414000000"),
        status = AppointmentStatusResponse.Booked
    )

    private val detailsOutput = DetailsOutput(
        id = "testServiceId",
        vin = "testVin",
        bookingId = "testDealerId",
        bookingLocation = "testLocation",
        scheduledTime = "2022-06-28T11:30Z",
        comment = "testFaultDescription",
        mileage = "200000",
        email = null,
        phone = "testTelephone",
        status = Status.Booked,
        reminders = listOf(MIN30),
        services = listOf(DetailsOutput.Service(id = "testServiceList", type = ServiceType.Generic))
    )

    @Test
    fun `transformOutput should return HistoryOutput with appointments`() {
        // given
        val appointmentHistoryLatamResponse = AppointmentHistoryEMEAResponse(
            appointments = listOf(
                Appointment(
                    serviceId = "serviceIdTest",
                    scheduledTime = "2023-09-29T12:50:4100Z",
                    status = AppointmentStatusResponse.Requested
                )
            )
        )

        // when
        val historyOutput = mapper.transformOutput(appointmentHistoryLatamResponse)

        // then
        Assert.assertEquals(
            "HistoryOutput(appointments=[Appointment(appointmentId=serviceIdTest, " +
                "scheduledTime=2023-09-29T12:50:00.410Z, status=Requested)])",
            historyOutput.toString()
        )
    }

    @Test
    fun `transformOutput should return HistoryOutput with empty appointments`() {
        // given
        val appointmentHistoryLatamResponse = AppointmentHistoryEMEAResponse(
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
            serviceId = "serviceIdTest",
            scheduledTime = "2023-09-29T12:50:4100Z",
            status = AppointmentStatusResponse.Requested
        )

        // when
        val historyOutput = mapper.transformItem(appointmentHistoryLatamResponse)

        // then
        Assert.assertEquals(
            "Appointment(appointmentId=serviceIdTest, scheduledTime=2023-09-29T12:50:00.410Z, status=Requested)",
            historyOutput.toString()
        )
    }

    @Test
    fun `transformAppointment should return HistoryOutput with appointment without scheduledTime`() {
        // given
        val appointmentHistoryLatamResponse = Appointment(
            serviceId = "serviceIdTest",
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
        val time = "2023-09-29T12:50:4100+02:00"

        // when
        val result = mapper.transformScheduledTime(time)

        // then
        Assert.assertEquals("2023-09-29T12:50:00.410+02:00", result?.toString())
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
        val time = "-1L"

        // when
        val result = mapper.transformScheduledTime(time)

        // then
        Assert.assertEquals(null, result)
    }

    @Test
    fun `when execute  transformAppointment then return a DetailsOutput`() {
        val result = mapper.transformOutput(detailsResponse)
        Assert.assertEquals(detailsOutput, result)
    }

    @Test
    fun `when execute getReminderValue then return a ReminderElements`() {
        val result = mapper.getReminderValue(172800000)
        Assert.assertEquals(Reminder.DAY2, result)
    }

    @Test
    fun `when execute getReminderValue with invalid value then return a null`() {
        val result = mapper.getReminderValue(0)
        Assert.assertEquals(null, result)
    }

    @Test
    fun `when execute transformReminder with valid value then return a ReminderItem`() {
        val result =
            mapper.transformReminder(
                listOf("1656414000000"),
                OffsetDateTime.of(2022, 6, 28, 11, 30, 0, 0, ZoneOffset.UTC)
            )
        Assert.assertEquals(detailsOutput.reminders, result)
    }

    @Test
    fun `when execute transformReminder with invalid value then return a null`() {
        val result = mapper.transformReminder(null, null)
        Assert.assertEquals(null, result)
    }

    @Test
    fun `when execute transformService with valid value then return a Service`() {
        val result = mapper.transformService(Service("testServiceList"))
        Assert.assertEquals(DetailsOutput.Service(id = "testServiceList", type = ServiceType.Generic), result)
    }
}
