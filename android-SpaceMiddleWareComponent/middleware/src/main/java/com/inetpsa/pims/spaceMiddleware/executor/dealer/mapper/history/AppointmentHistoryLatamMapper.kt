package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.transformStatus
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Reminder
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsLatamResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsLatamResponse.Mileage
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryLATAMResponse
import java.time.DateTimeException
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

internal class AppointmentHistoryLatamMapper {

    fun transformOutput(response: AppointmentHistoryLATAMResponse): HistoryOutput {
        val appointments = response.appointments
            ?.mapNotNull { transformItem(it) }
            ?.sortedByDescending { it.scheduledTime }
        return HistoryOutput(appointments)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformItem(response: AppointmentHistoryLATAMResponse.Appointment): HistoryOutput.Appointment? =
        transformScheduledTime(response.scheduledTime)?.let { scheduledTime ->
            HistoryOutput.Appointment(
                appointmentId = response.appointmentId,
                scheduledTime = scheduledTime.toString(),
                status = response.status.transformStatus()
            )
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformScheduledTime(time: Long?): OffsetDateTime? =
        time?.let {
            try {
                OffsetDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
            } catch (@Suppress("SwallowedException") e: DateTimeException) {
                null
            }
        }

    fun transformOutput(
        response: AppointmentDetailsLatamResponse,
        input: DetailsInput,
        dealerId: String
    ): DetailsOutput {
        val scheduledTime = transformScheduledTime(response.scheduledTime)
        val services = transformServices(response.services)
        return DetailsOutput(
            id = input.id,
            vin = input.vin,
            bookingId = dealerId,
            bookingLocation = null,
            scheduledTime = scheduledTime?.toString(),
            comment = null,
            mileage = transformMileage(response.mileage),
            email = response.customer?.email,
            phone = null,
            status = response.status.transformStatus(),
            services = services?.services,
            amount = services?.amount,
            reminders = transformReminder(response.reminders, scheduledTime).orEmpty()
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun transformReminder(reminders: List<String>?, scheduledTime: OffsetDateTime?): List<Reminder>? =
        scheduledTime?.toInstant()?.toEpochMilli()?.let { time ->
            println("scheduledTime: $scheduledTime")
            println("time: $time")
            reminders?.mapNotNull { reminder ->
                reminder.toLongOrNull()
                    ?.let {
                        println("toCheck: ${time - it}")
                        getReminderValue(time - it)
                    }
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getReminderValue(reminderValue: Long): Reminder? =
        Reminder.values().firstOrNull { it.timeLong == reminderValue }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformMileage(response: Mileage?): String? =
        response?.let { "${response.value} ${response.unitsKind}" }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformServices(response: AppointmentDetailsLatamResponse.Services?): Services? {
        var amount = 0f
        val dealerServices = response?.drs?.map {
            amount += it.price ?: 0f
            DetailsOutput.Service(
                id = it.id.toString(),
                name = it.name,
                comment = it.comment,
                opCode = it.opCode,
                price = it.price
            )
        }
        val factoryServices = response?.frs?.map {
            amount += it.price ?: 0f
            DetailsOutput.Service(
                id = it.id.toString(),
                name = it.name,
                comment = it.comment,
                opCode = it.opCode,
                price = it.price
            )
        }
        val repairServices = response?.repair?.map {
            amount += it.price ?: 0f
            DetailsOutput.Service(
                id = it.id.toString(),
                name = it.name,
                comment = it.comment,
                opCode = it.opCode,
                price = it.price
            )
        }
        val recallServices = response?.recalls?.map {
            amount += it.price ?: 0f
            DetailsOutput.Service(
                id = it.id.toString(),
                name = it.name,
                comment = it.comment,
                opCode = it.opCode,
                price = it.price
            )
        }

        val services = dealerServices.orEmpty() +
            factoryServices.orEmpty() +
            repairServices.orEmpty() +
            recallServices.orEmpty()

        return Services(services = services, amount = amount)
            .takeIf { it.services.isNotEmpty() }
    }

    internal data class Services(val services: List<DetailsOutput.Service>, val amount: Float)
}
