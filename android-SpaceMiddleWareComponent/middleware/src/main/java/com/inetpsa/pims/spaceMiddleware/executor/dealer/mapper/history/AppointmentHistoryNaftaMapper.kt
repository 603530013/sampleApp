package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.transformStatus
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsNaftaInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsNaftaResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryNAFTAResponse
import java.time.DateTimeException
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

internal class AppointmentHistoryNaftaMapper {

    fun transformOutput(response: AppointmentHistoryNAFTAResponse): HistoryOutput {
        val appointments = response.appointments
            ?.mapNotNull { transformItem(it) }
            ?.sortedByDescending { it.scheduledTime }
        return HistoryOutput(appointments)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformItem(response: AppointmentHistoryNAFTAResponse.Appointment): HistoryOutput.Appointment? =
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

    fun transformOutput(response: AppointmentDetailsNaftaResponse, input: DetailsNaftaInput): DetailsOutput {
        val scheduledTime = transformScheduledTime(response.scheduledTime)
        val services = transformServices(response.services)
        return DetailsOutput(
            id = input.id,
            vin = input.vin,
            bookingId = input.dealerId,
            scheduledTime = scheduledTime?.toString(),
            comment = response.customerConcernsInfo,
            mileage = transformMileage(response.mileage),
            email = null,
            phone = null,
            status = response.status.transformStatus(),
            services = services,
            amount = response.services?.summary?.total,
            reminders = null
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformMileage(response: AppointmentDetailsNaftaResponse.Mileage?): String? =
        response?.let { "${response.value} ${response.unitsKind}" }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformServices(response: AppointmentDetailsNaftaResponse.Services?): List<DetailsOutput.Service>? {
        val dealerServices = response?.drs?.map {
            DetailsOutput.Service(
                id = it.id.toString(),
                name = it.name,
                comment = it.comment,
                price = it.price
            )
        }
        val factoryServices = response?.frs?.map {
            DetailsOutput.Service(
                id = it.id.toString(),
                name = it.name,
                comment = it.comment,
                price = it.price
            )
        }
        val repairServices = response?.repair?.map {
            DetailsOutput.Service(
                id = it.id.toString(),
                name = it.name,
                comment = it.comment,
                price = it.price
            )
        }
        val recallServices = response?.recalls?.map {
            DetailsOutput.Service(
                id = it.id.toString(),
                name = it.name,
                comment = it.comment,
                price = it.price
            )
        }

        val services = dealerServices.orEmpty() +
            factoryServices.orEmpty() +
            repairServices.orEmpty() +
            recallServices.orEmpty()
        return services.takeIf { it.isNotEmpty() }
    }
}
