package com.mobiledrivetech.external.common.component.extensions

import androidx.annotation.VisibleForTesting
import java.math.RoundingMode
import java.math.RoundingMode.CEILING
import java.math.RoundingMode.DOWN
import java.math.RoundingMode.FLOOR
import java.math.RoundingMode.HALF_EVEN
import java.math.RoundingMode.HALF_UP
import java.text.DecimalFormat
import java.util.Locale

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun Any?.format(
    minimumFractionDigits: Int = 2,
    maximumFractionDigits: Int = 2,
    roundingMode: RoundingMode = HALF_EVEN,
    isGroupingUsed: Boolean = true,
    locale: Locale? = null
) = when (this) {
    is Number -> try {
        (locale?.let { DecimalFormat.getInstance(locale) } ?: DecimalFormat.getInstance()).apply {
            minimumIntegerDigits = 1
            this.minimumFractionDigits = minimumFractionDigits
            this.maximumFractionDigits = maximumFractionDigits
            this.roundingMode = roundingMode
            this.isGroupingUsed = isGroupingUsed
        }.format(this)
    } catch (ex: Exception) {
        EMPTY_VALUE_DASH
    }

    else -> EMPTY_VALUE_DASH
}

fun Any?.formatSpeed(): String = format(minimumFractionDigits = 0, maximumFractionDigits = 0, roundingMode = HALF_EVEN)

fun Any?.formatMileage(): String = format(
    minimumFractionDigits = 0,
    maximumFractionDigits = 0,
    roundingMode = HALF_EVEN
)

fun Any?.formatDistance(): String = format(minimumFractionDigits = 1, maximumFractionDigits = 1, roundingMode = DOWN)

fun Any?.formatConsumption(): String = format(minimumFractionDigits = 2, maximumFractionDigits = 2, roundingMode = DOWN)

fun Any?.formatPrice(): String = format(minimumFractionDigits = 2, maximumFractionDigits = 2, roundingMode = DOWN)

fun Any?.formatDistanceRoughly(): String = format(
    minimumFractionDigits = 0,
    maximumFractionDigits = 0,
    roundingMode = CEILING
)

fun Any?.formatDistanceRoundToOne(locale: Locale): String = format(
    minimumFractionDigits = 0,
    maximumFractionDigits = 1,
    roundingMode = HALF_UP,
    locale = locale
)

fun Any?.formatDistanceRoundDown(): String = format(
    minimumFractionDigits = 0,
    maximumFractionDigits = 0,
    roundingMode = FLOOR
)

fun Any?.formatDistanceRound(): String = format(
    minimumFractionDigits = 0,
    maximumFractionDigits = 0,
    roundingMode = HALF_UP
)
