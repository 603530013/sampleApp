package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AppointmentStatusResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.PackageResponse.Operations

internal fun AppointmentStatusResponse?.transformStatus(): Status? =
    when (this) {
        AppointmentStatusResponse.Requested -> Status.Requested
        AppointmentStatusResponse.Booked -> Status.Booked
        AppointmentStatusResponse.Cancelled -> Status.Cancelled
        AppointmentStatusResponse.Closed -> Status.Closed
        else -> null
    }

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun generateRequestOperation(operation: Operations): Map<String, Any?> =
    mapOf(
        Constants.Input.Appointment.REFERENCE to operation.code,
        Constants.Input.Appointment.TITLE to operation.title,
        Constants.Input.Appointment.TYPE to operation.type, // operation.type,
        Constants.Input.Appointment.PERIOD to 0, // ALWAYS 0, not used for the moment: MYM
        Constants.Input.Appointment.IS_PACKAGE to 0 // 0 if there is no package and 1 if there is a package
    )
