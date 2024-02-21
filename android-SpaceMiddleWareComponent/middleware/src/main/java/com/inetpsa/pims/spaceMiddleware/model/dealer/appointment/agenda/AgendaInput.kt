package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.BookingIdField
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput.TimeFence.MONTH
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput.TimeFence.SEMESTER
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput.TimeFence.WEEK
import java.time.LocalDate
import java.time.ZoneId

internal data class AgendaInput(
    override var dealerId: String,
    val dealerLocation: String? = null,
    val startDate: LocalDate,
    val timeFence: TimeFence,
    val vin: String,
    val serviceIds: String? = null
) : BookingIdField {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal companion object {

        const val WEEK_VALUE: Long = 1L
        const val MONTH_VALUE: Long = 1L
        const val SEMESTER_VALUE: Long = 6L
    }

    val endDate: LocalDate
        get() = when (timeFence) {
            WEEK -> startDate.plusWeeks(WEEK_VALUE)
            MONTH -> startDate.plusMonths(MONTH_VALUE)
            SEMESTER -> startDate.plusMonths(SEMESTER_VALUE)
        }

    val startDateMilliSeconds: Long
        get() = startDate
            .atStartOfDay(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()

    val endDateMilliSeconds: Long
        get() = endDate
            .atStartOfDay(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()

    enum class TimeFence { WEEK, MONTH, SEMESTER }
}
