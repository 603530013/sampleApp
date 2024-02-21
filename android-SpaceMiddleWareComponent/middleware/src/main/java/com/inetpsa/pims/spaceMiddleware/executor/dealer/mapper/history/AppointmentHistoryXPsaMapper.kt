package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CachedAppointmentsXPSA
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput

internal class AppointmentHistoryXPsaMapper {

    fun transformOutput(response: CachedAppointmentsXPSA): HistoryOutput =
        HistoryOutput(
            appointments = response.appointments.map {
                HistoryOutput.Appointment(
                    appointmentId = it.appointmentId,
                    scheduledTime = it.date,
                    status = transformStatus(it.status)
                )
            }
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformStatus(status: String?): Status? {
        return when (status) {
            Status.Booked.name -> Status.Booked
            Status.Requested.name -> Status.Requested
            Status.Cancelled.name -> Status.Cancelled
            Status.Closed.name -> Status.Closed
            else -> null
        }
    }
}
