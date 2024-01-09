package com.mobiledrivetech.external.common.base.extensions

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.mobiledrivetech.external.common.base.R
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.util.Locale

const val FORMAT = "%02dh%02d"
@RequiresApi(Build.VERSION_CODES.O)
infix fun ZonedDateTime.formatRangeTo(end: ZonedDateTime): String {
    val locale = Locale.getDefault()
    val startMonthFormatted = month.getDisplayName(TextStyle.SHORT, locale)
    val endMonthFormatted = end.month.getDisplayName(TextStyle.SHORT, locale)
    val startTime = String.format(Locale.getDefault(), FORMAT, hour, minute)
    val endTime = String.format(Locale.getDefault(), FORMAT, end.hour, end.minute)
    return when {
        monthValue != end.monthValue ->
            "$dayOfMonth $startMonthFormatted $startTime " +
                "- ${end.dayOfMonth} $endMonthFormatted $endTime"
        // 1 fev 7h15 - 3 mars 7h15
        monthValue == end.monthValue && dayOfMonth != end.dayOfMonth ->
            "$dayOfMonth - ${end.dayOfMonth} $startMonthFormatted $startTime - $endTime"
        // 1 - 3 fev 7h15 - 8h15
        else -> "$dayOfMonth $startMonthFormatted $startTime - $endTime"
        // 1 fev 7h15 - 8h15
    }
}

private const val ONE_SECOND: Long = 1
private const val ONE_MINUTE = ONE_SECOND * 60
private const val ONE_HOUR = ONE_MINUTE * 60
private const val ONE_DAY = ONE_HOUR * 24
private const val STRING_CAPACITY = 24

/**
 * Minutes per hour.
 */
const val MINUTES_PER_HOUR = 60

/**
 * Seconds per minute.
 */
const val SECONDS_PER_MINUTE = 60

/**
 * Seconds per hour.
 */
const val SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR

fun Long?.asDurationFormat(withDay: Boolean = true): String {
    val context = kGet<Context>()
    if (this == null) {
        return EMPTY_VALUE_DASH
    }

    fun generateString(value: Long?, @StringRes label: Int) =
        String.format(Locale.getDefault(), "%d%s", value?.toInt() ?: 0, context.getString(label))

    val items = mutableListOf<String>()
    var temp = this

    if (withDay) {
        val tempDay = temp / ONE_DAY
        if (tempDay > 0) {
            items.add(generateString(tempDay, R.string.common_unit_date_day_short))
            temp %= ONE_DAY
        }
    }

    val tempHour = temp / ONE_HOUR
    if (tempHour > 0) {
        items.add(generateString(tempHour, R.string.common_unit_date_hour_short))
        temp %= ONE_HOUR
    }

    val tempMinute = temp / ONE_MINUTE
    if (tempMinute > 0) {
        items.add(generateString(tempMinute, R.string.common_unit_date_minute_short))
        temp %= ONE_MINUTE
    }

    if (items.isEmpty()) {
        items.add("0 ${context.getString(R.string.common_unit_date_minute_short)}")
    }
    // 1D 1H 1Min
    return items.joinToString(separator = " ")
}

@RequiresApi(Build.VERSION_CODES.O)
infix fun ZonedDateTime?.asFormattedLastUpdate(context: Context): String = when (this) {
    null -> EMPTY_VALUE_DASH

    else -> {
        val date = withZoneSameInstant(ZoneId.systemDefault())
        val at = context.getString(R.string.common_date_at)
        val formatter = DateTimeFormatter.ofPattern("d LLL '$at' HH:mm")
        context.getString(R.string.common_state_lastUpdate, formatter.format(date))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.formatTime(): String = this.format(DateTimeFormatter.ofPattern("HH:mm"))

@RequiresApi(Build.VERSION_CODES.O)
fun String?.asDuration() = when (this.isNullOrBlank()) {
    true -> EMPTY_VALUE_DASH
    else -> try {
        Duration.parse(this).seconds.asDurationFormat()
    } catch (ex: DateTimeParseException) {
        Log.d("DateTimeExtensions", "$ex cannot transform '$this' to Duration")
        EMPTY_VALUE_DASH
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime?.asDurationFormat() = when (this) {
    null -> ""
    else -> Duration.ofHours(this.hour.toLong()).plusMinutes(this.minute.toLong()).format()
}

@RequiresApi(Build.VERSION_CODES.O)
fun Duration.format(): String {
    if (this === Duration.ZERO) {
        return "PT00H"
    }
    val effectiveTotalSecs = seconds

    val hours: Long = effectiveTotalSecs / SECONDS_PER_HOUR
    val minutes = effectiveTotalSecs % SECONDS_PER_HOUR / SECONDS_PER_MINUTE

    val buf = StringBuilder(STRING_CAPACITY)
    buf.append("PT")
    if (hours != 0L) {
        buf.append(String.format(Locale.getDefault(), "%02d", hours)).append('H')
    }
    if (minutes != 0L) {
        buf.append(String.format(Locale.getDefault(), "%02d", minutes)).append('M')
    }

    return buf.toString()
}
