package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history

import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Reminder
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsLatamResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryLATAMResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryLATAMResponse.Appointment
import io.mockk.every
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Collections

internal class AppointmentHistoryLatamMapperTest : FcaExecutorTestHelper() {

    private val mapper: AppointmentHistoryLatamMapper = spyk()

    @Test
    fun `transformOutput should return HistoryOutput with appointments`() {
        // given
        val appointmentHistoryLatamResponse = AppointmentHistoryLATAMResponse(
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
        val appointmentHistoryLatamResponse = AppointmentHistoryLATAMResponse(
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

    @Test
    fun `transformServices should return HistoryOutput with services`() {
        // given
        val services = listOf(
            AppointmentDetailsLatamResponse.Services.Service(
                id = 1234,
                name = "serviceNameTest",
                comment = "serviceCommentTest",
                price = 100F,
                opCode = "testOpCode",
                selected = true
            )
        )

        // when
        val result = mapper.transformServices(
            AppointmentDetailsLatamResponse.Services(
                drs = services,
                frs = services,
                repair = services,
                recalls = services
            )
        )

        // then
        Assert.assertEquals(
            "Services(services=[Service(id=1234, comment=serviceCommentTest, type=null, " +
                "name=serviceNameTest, opCode=testOpCode, price=100.0), Service(id=1234, " +
                "comment=serviceCommentTest, type=null, name=serviceNameTest, opCode=testOpCode, " +
                "price=100.0), Service(id=1234, comment=serviceCommentTest, type=null, name=serviceNameTest, " +
                "opCode=testOpCode, price=100.0), Service(id=1234, comment=serviceCommentTest, type=null, " +
                "name=serviceNameTest, opCode=testOpCode, price=100.0)], amount=400.0)",
            result.toString()
        )
    }

    @Test
    fun `transformServices should return HistoryOutput with services empty`() {
        // given
        val services = AppointmentDetailsLatamResponse.Services(
            drs = emptyList(),
            frs = emptyList(),
            repair = emptyList(),
            recalls = emptyList()
        )

        // when
        val result = mapper.transformServices(services)

        // then
        Assert.assertNull(result)
    }

    @Test
    fun `transformServices should return HistoryOutput with services null`() {
        // given
        val services = null

        // when
        val result = mapper.transformServices(services)

        // then
        Assert.assertEquals(null, result)
    }

    @Test
    fun `transformServices should return HistoryOutput with services without price`() {
        // given
        val services = listOf(
            AppointmentDetailsLatamResponse.Services.Service(
                id = 1234,
                name = "serviceNameTest",
                comment = "serviceCommentTest",
                price = null
            )
        )

        // when
        val result = mapper.transformServices(
            AppointmentDetailsLatamResponse.Services(
                drs = services,
                frs = services,
                repair = services,
                recalls = services
            )
        )

        // then
        Assert.assertEquals(
            "Services(services=[Service(id=1234, comment=serviceCommentTest, type=null, " +
                "name=serviceNameTest, opCode=null, price=null), Service(id=1234, comment=serviceCommentTest," +
                " type=null, name=serviceNameTest, opCode=null, price=null), Service(id=1234," +
                " comment=serviceCommentTest, type=null, name=serviceNameTest, opCode=null, price=null)," +
                " Service(id=1234, comment=serviceCommentTest, type=null, name=serviceNameTest, opCode=null, " +
                "price=null)], amount=0.0)",
            result.toString()
        )
    }

    @Test
    fun `transformServices should return HistoryOutput with services without selected`() {
        // given
        val services = listOf(
            AppointmentDetailsLatamResponse.Services.Service(
                id = 1234,
                name = "serviceNameTest",
                comment = "serviceCommentTest",
                price = null,
                selected = null
            )
        )

        // when
        val result = mapper.transformServices(
            AppointmentDetailsLatamResponse.Services(
                drs = services,
                frs = services,
                repair = services,
                recalls = services
            )
        )

        // then
        Assert.assertEquals(
            "Services(services=[Service(id=1234, comment=serviceCommentTest, type=null, " +
                "name=serviceNameTest, opCode=null, price=null), Service(id=1234, comment=serviceCommentTest," +
                " type=null, name=serviceNameTest, opCode=null, price=null), Service(id=1234, " +
                "comment=serviceCommentTest, type=null, name=serviceNameTest, opCode=null, price=null), " +
                "Service(id=1234, comment=serviceCommentTest, type=null, name=serviceNameTest, opCode=null," +
                " price=null)], amount=0.0)",
            result.toString()
        )
    }

    @Test
    fun `transformServices should return HistoryOutput with services without opCode`() {
        // given
        val services = listOf(
            AppointmentDetailsLatamResponse.Services.Service(
                id = 1234,
                name = "serviceNameTest",
                comment = "serviceCommentTest",
                price = null,
                opCode = null
            )
        )

        // when
        val result = mapper.transformServices(
            AppointmentDetailsLatamResponse.Services(
                drs = services,
                frs = services,
                repair = services,
                recalls = services
            )
        )

        // then
        Assert.assertEquals(
            "Services(services=[Service(id=1234, comment=serviceCommentTest, type=null," +
                " name=serviceNameTest, opCode=null, price=null), Service(id=1234, comment=serviceCommentTest," +
                " type=null, name=serviceNameTest, opCode=null, price=null), Service(id=1234," +
                " comment=serviceCommentTest, type=null, name=serviceNameTest, opCode=null, price=null)," +
                " Service(id=1234, comment=serviceCommentTest, type=null, name=serviceNameTest, opCode=null," +
                " price=null)], amount=0.0)",
            result.toString()
        )
    }

    @Test
    fun `transformMileage should return HistoryOutput with mileage`() {
        // given
        val mileage = AppointmentDetailsLatamResponse.Mileage(
            value = 1000.0,
            unitsKind = "testUnitsKind"
        )

        // when
        val result = mapper.transformMileage(mileage)

        // then
        Assert.assertEquals("1000.0 testUnitsKind", result)
    }

    @Test
    fun `transformMileage should return HistoryOutput with mileage null`() {
        // given
        val mileage = null

        // when
        val result = mapper.transformMileage(mileage)

        // then
        Assert.assertEquals(null, result)
    }

    @Test
    fun `transformMileage should return HistoryOutput with mileage without unitsKind`() {
        // given
        val mileage = AppointmentDetailsLatamResponse.Mileage(
            value = 1000.0,
            unitsKind = null
        )

        // when
        val result = mapper.transformMileage(mileage)

        // then
        Assert.assertEquals("1000.0 null", result)
    }

    @Test
    fun `getReminderValue should return Reminder with timeLong 0`() {
        // given
        val reminderValue = 0L

        // when
        val result = mapper.getReminderValue(reminderValue)

        // then
        Assert.assertNull(result)
    }

    @Test
    fun `getReminderValue should return Reminder with timeLong 600000`() {
        // given
        val reminderValue = 600000L

        // when
        val result = mapper.getReminderValue(reminderValue)

        // then
        Assert.assertEquals(Reminder.MIN10, result)
    }

    @Test
    fun `getReminderValue should return Reminder with timeLong 1800000`() {
        // given
        val reminderValue = 1800000L

        // when
        val result = mapper.getReminderValue(reminderValue)

        // then
        Assert.assertEquals(Reminder.MIN30, result)
    }

    @Test
    fun `getReminderValue should return Reminder with timeLong 3600000`() {
        // given
        val reminderValue = 3600000L

        // when
        val result = mapper.getReminderValue(reminderValue)

        // then
        Assert.assertEquals(Reminder.HOUR1, result)
    }

    @Test
    fun `getReminderValue should return Reminder with timeLong 86400000`() {
        // given
        val reminderValue = 86400000L

        // when
        val result = mapper.getReminderValue(reminderValue)

        // then
        Assert.assertEquals(Reminder.DAY1, result)
    }

    @Test
    fun `getReminderValue should return Reminder with timeLong 604800000`() {
        // given
        val reminderValue = 604800000L

        // when
        val result = mapper.getReminderValue(reminderValue)

        // then
        Assert.assertEquals(Reminder.DAY7, result)
    }

    @Test
    fun `getReminderValue should return Reminder with timeLong 2592000000`() {
        // given
        val reminderValue = 2592000000L

        // when
        val result = mapper.getReminderValue(reminderValue)

        // then
        Assert.assertNull(result)
    }

    @Test
    fun `transformReminder should return HistoryOutput with reminders`() {
        val epoch = 1656414000000L
        // given
        val reminders = listOf(
            "0",
            (epoch - 600000).toString(),
            (epoch - 1800000).toString(),
            (epoch - 3600000).toString(),
            (epoch - 86400000).toString(),
            (epoch - 604800000).toString(),
            (epoch - 2592000000).toString()
        )
        val scheduledTime = Instant.ofEpochMilli(epoch).atOffset(ZonedDateTime.now().offset)

        // when
        val result = mapper.transformReminder(reminders, scheduledTime)

        // then
        Assert.assertEquals(
            "[MIN10, MIN30, HOUR1, DAY1, DAY7]",
            result.toString()
        )
    }

    @Test
    fun `transformReminder should return HistoryOutput with reminders null`() {
        // given
        val reminders = null
        val epoch = 1656414000000L
        val scheduledTime = Instant.ofEpochMilli(epoch).atOffset(ZoneOffset.UTC)

        // when
        val result = mapper.transformReminder(reminders, scheduledTime)

        // then
        Assert.assertEquals(null, result)
    }

    @Test
    fun `transformReminder should return HistoryOutput with reminders empty`() {
        // given
        val reminders = emptyList<String>()
        val epoch = 1656414000000L
        val scheduledTime = Instant.ofEpochMilli(epoch).atOffset(ZoneOffset.UTC)

        // when
        val result = mapper.transformReminder(reminders, scheduledTime)

        // then
        Assert.assertEquals(emptyList<Reminder>(), result)
    }

    @Suppress("LongMethod")
    @Test
    fun `transformOutput should return DetailsOutput`() {
        every { mapper.transformReminder(any(), any()) } returns listOf(
            Reminder.MIN10,
            Reminder.MIN30,
            Reminder.HOUR1,
            Reminder.DAY1,
            Reminder.DAY7
        )

        every { mapper.transformMileage(any()) } returns "1000.0 testUnitsKind"
        // given
        val appointmentDetailsLatamResponse = AppointmentDetailsLatamResponse(
            scheduledTime = 1656414000000,
            services = AppointmentDetailsLatamResponse.Services(
                drs = listOf(
                    AppointmentDetailsLatamResponse.Services.Service(
                        id = 1234,
                        name = "serviceNameTest",
                        comment = "serviceCommentTest",
                        price = 100F,
                        opCode = "testOpCode",
                        selected = true
                    )
                ),
                frs = listOf(
                    AppointmentDetailsLatamResponse.Services.Service(
                        id = 1234,
                        name = "serviceNameTest",
                        comment = "serviceCommentTest",
                        price = 100F,
                        opCode = "testOpCode",
                        selected = true
                    )
                ),
                repair = listOf(
                    AppointmentDetailsLatamResponse.Services.Service(
                        id = 1234,
                        name = "serviceNameTest",
                        comment = "serviceCommentTest",
                        price = 100F,
                        opCode = "testOpCode",
                        selected = true
                    )
                ),
                recalls = listOf(
                    AppointmentDetailsLatamResponse.Services.Service(
                        id = 1234,
                        name = "serviceNameTest",
                        comment = "serviceCommentTest",
                        price = 100F,
                        opCode = "testOpCode",
                        selected = true
                    )
                )
            ),
            mileage = AppointmentDetailsLatamResponse.Mileage(
                value = 1000.0,
                unitsKind = "testUnitsKind"
            ),
            customer = AppointmentDetailsLatamResponse.Customer(
                email = "testEmail"
            ),
            status = AppointmentStatusResponse.Requested,
            reminders = listOf(
                "0",
                "600000",
                "1800000",
                "3600000",
                "86400000",
                "604800000",
                "2592000000"
            )
        )
        val input = DetailsInput(
            id = "idTest",
            vin = "vinTest"
        )

        // when
        val result = mapper.transformOutput(appointmentDetailsLatamResponse, input, "dealerIdTest")

        // then
        Assert.assertEquals(
            "DetailsOutput(id=idTest, vin=vinTest, bookingId=dealerIdTest, bookingLocation=null, " +
                "scheduledTime=2022-06-28T11:00Z, comment=null, mileage=1000.0 testUnitsKind, email=testEmail, " +
                "phone=null, status=Requested, services=[Service(id=1234, comment=serviceCommentTest, type=null, " +
                "name=serviceNameTest, opCode=testOpCode, price=100.0), Service(id=1234, " +
                "comment=serviceCommentTest, type=null, name=serviceNameTest, opCode=testOpCode, price=100.0), " +
                "Service(id=1234, comment=serviceCommentTest, type=null, name=serviceNameTest, " +
                "opCode=testOpCode, price=100.0), Service(id=1234, comment=serviceCommentTest, type=null, " +
                "name=serviceNameTest, opCode=testOpCode, price=100.0)], amount=400.0, " +
                "reminders=[MIN10, MIN30, HOUR1, DAY1, DAY7])",
            result.toString()
        )
    }

    @Test
    fun `transformScheduledTime should return HistoryOutput Appointment ScheduledTime null with negative time`() {
        // given
        val time = -1L

        // when
        val result = mapper.transformScheduledTime(time)

        // then
        Assert.assertEquals("1969-12-31T23:59:59.999Z", result.toString())
    }

    @Test
    fun `transformScheduledTime should return HistoryOutput Appointment ScheduledTime null with null time`() {
        // given
        val time = null

        // when
        val result = mapper.transformScheduledTime(time)

        // then
        Assert.assertEquals(null, result)
    }
}
