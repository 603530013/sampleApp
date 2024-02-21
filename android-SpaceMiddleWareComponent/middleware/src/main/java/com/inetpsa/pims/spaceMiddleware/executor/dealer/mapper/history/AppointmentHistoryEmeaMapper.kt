package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.transformStatus
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Reminder
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsEmeaResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsEmeaResponse.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryEMEAResponse
import com.inetpsa.pims.spaceMiddleware.util.read
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

@Suppress("TooManyFunctions")
internal class AppointmentHistoryEmeaMapper {

    companion object {

        private const val EPOCH_MILLISECONDS_LENGTH = 12
    }

    private val dateTimeFormatter = DateTimeFormatterBuilder()
        .parseLenient()
        .parseCaseInsensitive()
        .appendPattern("yyyy-MM-dd'T'HH:mm")
        .appendOptional(DateTimeFormatter.ofPattern(":SSSS"))
        .optionalStart()
        .appendZoneId()
        .optionalEnd()
        .toFormatter()

    fun transformOutput(response: AppointmentHistoryEMEAResponse): HistoryOutput {
        val appointments = response.appointments
            ?.mapNotNull { transformItem(it) }
            ?.sortedByDescending { it.scheduledTime }
        return HistoryOutput(appointments)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformItem(response: AppointmentHistoryEMEAResponse.Appointment): HistoryOutput.Appointment? =
        transformScheduledTime(response.scheduledTime)?.let { scheduledTime ->
            HistoryOutput.Appointment(
                appointmentId = response.serviceId,
                scheduledTime = scheduledTime.toString(),
                status = response.status.transformStatus()
            )
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformScheduledTime(time: String?): OffsetDateTime? =
        transformCustomPattern(time)
            ?: transformLocalDateTime(time)
            ?: transformEpoch(time)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformCustomPattern(time: String?): OffsetDateTime? =
        time?.let {
            try {
                val temporal = dateTimeFormatter.parse(it)

                val hour = temporal read ChronoField.HOUR_OF_DAY
                val minute = temporal read ChronoField.MINUTE_OF_HOUR
                val seconds = temporal read ChronoField.SECOND_OF_MINUTE
                val nanoSeconds = temporal read ChronoField.NANO_OF_SECOND
                val zoneOffset = ZoneOffset.from(temporal)
                val localDate = LocalDate.from(temporal)
                val localTime = LocalTime.of(hour, minute, seconds, nanoSeconds)
                OffsetDateTime.of(localDate, localTime, zoneOffset)
            } catch (@Suppress("SwallowedException") e: DateTimeException) {
                e.printStackTrace()
                null
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformLocalDateTime(time: String?): OffsetDateTime? =
        time?.let {
            try {
                OffsetDateTime.parse(time)
            } catch (@Suppress("SwallowedException") e: DateTimeException) {
                e.printStackTrace()
                null
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformEpoch(time: String?): OffsetDateTime? =
        if ((time?.length ?: 0) >= EPOCH_MILLISECONDS_LENGTH) {
            transformEpochMilliSeconds(time)
        } else {
            transformEpochSeconds(time)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformEpochSeconds(time: String?): OffsetDateTime? =
        time?.toLongOrNull()?.let {
            try {
                Instant.ofEpochSecond(it).atOffset(ZoneOffset.UTC)
            } catch (@Suppress("SwallowedException") e: DateTimeException) {
                e.printStackTrace()
                null
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformEpochMilliSeconds(time: String?): OffsetDateTime? =
        time?.toLongOrNull()?.let {
            try {
                Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC)
            } catch (@Suppress("SwallowedException") e: DateTimeException) {
                e.printStackTrace()
                null
            }
        }

    fun transformOutput(response: AppointmentDetailsEmeaResponse): DetailsOutput {
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
    internal fun transformService(service: Service?): DetailsOutput.Service? =
        service?.description?.let { DetailsOutput.Service(id = it, type = ServiceType.Generic) }
}
