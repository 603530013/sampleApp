package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.ChannelFeature
import com.inetpsa.pims.spaceMiddleware.util.filterNotNull
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.util.Locale

internal class DealerFcaMapper(private val vehicle: VehicleResponse) {

    internal fun transformDealer(response: DealerDetailsResponse): DealerOutput {
        val bookingId = parseOnlineBookingId(response)
        return DealerOutput(
            id = response.id,
            name = response.name,
            address = response.address,
            latitude = response.latitude,
            longitude = response.longitude,
            phones = response.phoneNumber.takeIf { !it.isNullOrBlank() }?.let { mapOf(Constants.DEFAULT to it) },
            bookingId = bookingId?.first,
            bookingLocation = bookingId?.second,
            preferred = response.preferred ?: false,
            emails = null,
            website = response.website,
            bookable = bookingId != null,
            openingHours = transformDepartments(response.departments),
            services = response.services?.asSequence()
                ?.mapNotNull { it.services }
                ?.flatten()
                ?.toHashSet()
                ?.map { Service(code = it, type = null, label = null) }
                ?.sortedBy { it.code }?.toList()

        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun parseOnlineBookingId(response: DealerDetailsResponse): Pair<String, String?>? {
        // GSP-15546 Hide "Schedule service" button for dealers in Netherlands and Belgium
        // only Maserati will receive serviceScheduling parameter, for other brands the nil check will fail
        if (response.serviceScheduling == false) {
            return null
        }

        val serviceSchedulerCase: ServiceSchedulerCase = transformChannelFeatures(vehicle)

        val dealerId = when (serviceSchedulerCase) {
            ServiceSchedulerCase.EMEA, ServiceSchedulerCase.LATAM, ServiceSchedulerCase.MASERATI -> response.id
            ServiceSchedulerCase.NAFTA, ServiceSchedulerCase.V1 -> response.ossDealerId
            ServiceSchedulerCase.NONE -> null
        }

        return dealerId.takeIf { !it.isNullOrBlank() }
            ?.let {
                when (serviceSchedulerCase) {
                    ServiceSchedulerCase.EMEA, ServiceSchedulerCase.LATAM, ServiceSchedulerCase.MASERATI -> {
                        val extraction = it.split("|")
                        Pair(extraction.first(), extraction.getOrNull(1))
                    }

                    else -> Pair(it, null)
                }
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformDepartments(departments: Map<String, Map<String, DealerDetailsResponse.OpeningHours?>?>?):
        Map<String, Map<String, DealerOutput.OpeningHour>>? =
        departments
            ?.filterNotNull()
            ?.mapValues { department ->
                department.value
                    .entries
                    .associate { parseDayOfWeek(it.key)?.name.orEmpty() to transformOpeningHours(it.value) }
                    .filterNotNull()
            }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun parseDayOfWeek(day: String?): DayOfWeek? =
        day?.takeIf { it.isNotBlank() }
            ?.let {
                try {
                    val pattern = DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("EEEE")
                        .toFormatter(Locale.US)
                    val parsedDay = pattern.parse(day)

                    DayOfWeek.from(parsedDay)
                } catch (ex: DateTimeParseException) {
                    println(ex.message)
                    PIMSLogger.w(ex, "Fail to parse day of week of $day")
                    null
                }
            }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformOpeningHours(openingHour: DealerDetailsResponse.OpeningHours?): DealerOutput.OpeningHour? =
        openingHour?.let { hours ->
            val pattern = DateTimeFormatter.ofPattern("HH:mm")
            val open: LocalTime? = extractTime(hours.open, hours.hour24 ?: true)
            val close: LocalTime? = extractTime(hours.close, hours.hour24 ?: true)
            if (open != null && close != null) {
                val isClosed = hours.closed ?: true
                DealerOutput.OpeningHour(
                    closed = isClosed,
                    time = isClosed.takeIf { !it }?.let { listOf("${pattern.format(open)}-${pattern.format(close)}") }
                )
            } else {
                null
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun extractTime(
        indication: DealerDetailsResponse.OpeningHours.HourIndication?,
        is24: Boolean
    ): LocalTime? =
        when (is24) {
            true -> indication?.time?.takeIf { it.isNotBlank() }?.let {
                val pattern = DateTimeFormatter.ofPattern("HH:mm")
                parseTime(it, pattern)
            }

            else -> indication?.time?.takeIf { it.isNotBlank() }?.let {
                val pattern = DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .parseLenient()
                    .appendPattern("KK:mm a")
                    .toFormatter(Locale.US)
                parseTime("$it ${indication.ampm.orEmpty()}", pattern)
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun parseTime(time: String, formatter: DateTimeFormatter): LocalTime? = try {
        LocalTime.parse(time, formatter)
    } catch (ex: DateTimeParseException) {
        PIMSLogger.w(ex, "fail parsing time: $time")
        null
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformChannelFeatures(vehicle: VehicleResponse): ServiceSchedulerCase =
        vehicle.channelFeatures
            ?.map { transformChannelFeature(it, vehicle.market) }
            ?.firstOrNull() { it != ServiceSchedulerCase.NONE } ?: ServiceSchedulerCase.NONE

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformChannelFeature(channelFeature: ChannelFeature?, market: String?): ServiceSchedulerCase {
        val isGma = channelFeature?.channels?.any { it.equals("GMA", true) } ?: false
        return when {
            channelFeature?.featureCode.equals("f_0097_E", true) && isGma -> ServiceSchedulerCase.EMEA
            channelFeature?.featureCode.equals("f_0097_N", true) && isGma -> ServiceSchedulerCase.NAFTA
            channelFeature?.featureCode.equals("f_0097_L", true) && isGma -> ServiceSchedulerCase.LATAM
            channelFeature?.featureCode.equals("f_0097_ME", true) && isGma -> ServiceSchedulerCase.MASERATI
            channelFeature?.featureCode.equals("f_0097_MN", true) && isGma -> ServiceSchedulerCase.MASERATI
            Locale.US.country.equals(market, ignoreCase = true) -> ServiceSchedulerCase.V1
            else -> ServiceSchedulerCase.NONE
        }
    }
}
