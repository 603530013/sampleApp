package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.OpeningHours
import com.inetpsa.pims.spaceMiddleware.util.filterNotNull
import com.inetpsa.pims.spaceMiddleware.util.normaliseFromHtml
import java.time.DayOfWeek
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.util.Locale

internal class DealerPSAMapper {

    internal fun transformDealer(response: DetailsResponse, locale: Locale, preferred: Boolean): DealerOutput {
        return DealerOutput(
            id = response.siteGeo,
            name = response.name,
            address = transformAddress(response.address, locale),
            emails = extractInfo(response.emails, "Email", "Email"),
            latitude = response.coordinates?.latitude?.toString(),
            longitude = response.coordinates?.longitude?.toString(),
            phones = extractInfo(response.phones, "PhoneNumber", "Phone"),
            bookingId = response.siteGeo,
            bookingLocation = null,
            preferred = preferred,
            bookable = response.isCaracRdvi ?: false,
            website = response.websites?.public,
            openingHours = transformOpeningHour(response.openHours, locale),
            services = response.business?.mapNotNull { item ->
                item.code
                    .takeIf { !it.isNullOrBlank() }
                    ?.let {
                        Service(
                            code = it,
                            label = item.label,
                            type = item.type
                        )
                    }
            }?.sortedBy { it.code }
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformAddress(address: DetailsResponse.Address?, locale: Locale): String? =
        address?.let {
            listOfNotNull(address.address1, address.address2)
                .filter { it.isNotBlank() }
                .joinToString(" ")
                .takeIf { it.isNotBlank() }
                ?.let { street ->
                    val city = address.city?.replaceFirstChar { it.uppercaseChar() }
                    val zipCode = address.zipCode?.uppercase()
                    val zipCity = when (locale.country) {
                        Locale.UK.country -> listOfNotNull(city, zipCode)
                        else -> listOfNotNull(zipCode, city)
                    }.joinToString(" ")
                    "$street $zipCity"
                }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun extractInfo(data: Map<String, String>?, defaultKey: String, keyPrefix: String): Map<String, String>? {
        val default = data?.get(defaultKey)?.takeIf { it.isNotBlank() }
        return data?.filterNotNull()
            ?.filterKeys { it != defaultKey }
            ?.toMutableMap()
            ?.also { map -> default?.let { map[Constants.DEFAULT] = it } }
            ?.mapKeys { it.key.removePrefix(keyPrefix) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformOpeningHour(
        openingHours: List<OpeningHours>?,
        locale: Locale
    ): Map<String, Map<String, DealerOutput.OpeningHour>>? =
        openingHours?.takeIf { it.isNotEmpty() }
            ?.associate { it.type.orEmpty() to extractTime(it.label, locale) }
            ?.filterNotNull()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun extractTime(label: String?, locale: Locale): Map<String, DealerOutput.OpeningHour>? {
        PIMSLogger.d("label: $label, locale: $locale")
        val time = label.normaliseFromHtml()
            ?.trim()
            ?.split(System.lineSeparator())
            ?.map { it.trim() }
        PIMSLogger.d("time: $time")

        return time?.associate { label ->
            val dayOfWeek = parseDayOfWeek(label.substringBefore(":", ""), locale)
            val hours = label.substringAfter(":", "")
            val splitHours = hours.split(" ").filter { hasValidTime(it) }
            val isClosed = splitHours.isEmpty()
            val opening = isClosed.takeIf { !it }?.let { splitHours }
            PIMSLogger.d(
                "dayOfWeek: $dayOfWeek, hours: $hours, splitHours:$splitHours, isClosed: $isClosed, " +
                    "opening: $opening"
            )
            dayOfWeek?.name.orEmpty() to DealerOutput.OpeningHour(isClosed, opening)
        }?.filterNotNull()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun parseDayOfWeek(day: String?, locale: Locale): DayOfWeek? =
        day?.takeIf { it.isNotBlank() }
            ?.let {
                try {
                    val pattern = DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("EEEE")
                        .toFormatter(locale)
                    val parsedDay = pattern.parse(day)
                    DayOfWeek.from(parsedDay)
                } catch (ex: DateTimeParseException) {
                    PIMSLogger.w(ex, "Fail to parse day of week of $day")
                    null
                }
            }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun hasValidTime(time: String?): Boolean =
        time?.let { "^\\d{2}:\\d{2}-\\d{2}:\\d{2}\$".toRegex() matches it } ?: false
}
