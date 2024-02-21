package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.transformStatus
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Reminder
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsMaseratiResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryMaseratiResponse
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeParseException

internal class AppointmentHistoryMaseratiMapper {

    fun transformOutput(response: AppointmentHistoryMaseratiResponse): HistoryOutput {
        val appointments = response.appointments
            ?.mapNotNull { transformItem(it) }
            ?.sortedByDescending { it.scheduledTime }
        return HistoryOutput(appointments)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformItem(response: AppointmentHistoryMaseratiResponse.Appointment): HistoryOutput.Appointment? =
        transformScheduledTime(response.scheduledTime)?.let { scheduledTime ->
            HistoryOutput.Appointment(
                appointmentId = response.serviceId,
                scheduledTime = scheduledTime.toString(),
                status = response.status.transformStatus()
            )
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformScheduledTime(time: String?): OffsetDateTime? =
        time?.let {
            try {
                LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            } catch (e: DateTimeParseException) {
                null
            }
        }

    fun transformOutput(response: AppointmentDetailsMaseratiResponse): DetailsOutput {
        val scheduledTime = transformScheduledTime(response.scheduledTime)
        return DetailsOutput(
            id = response.serviceId,
            vin = response.vin,
            bookingId = response.dealerId,
            bookingLocation = response.location,
            scheduledTime = scheduledTime?.toString(),
            reminders = transformReminder(response.reminders, scheduledTime).orEmpty(),
            comment = response.faultDescription,
            mileage = response.vehicleKm,
            email = null,
            phone = response.telephone,
            status = response.status.transformStatus(),
            services = response.servicesList?.mapNotNull { transformService(it) },
            amount = null
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun transformReminder(reminders: List<String>?, scheduledTime: OffsetDateTime?): List<Reminder>? =
        scheduledTime?.toInstant()?.toEpochMilli()?.let { time ->
            reminders?.mapNotNull { reminder ->
                reminder.toLongOrNull()?.let { getReminderValue(time - it) }
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getReminderValue(reminderValue: Long): Reminder? =
        Reminder.values().firstOrNull { it.timeLong == reminderValue }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformService(service: AppointmentDetailsMaseratiResponse.Service?): DetailsOutput.Service? =
        service?.let { DetailsOutput.Service(id = it.code, name = it.description, type = ServiceType.Generic) }
}
