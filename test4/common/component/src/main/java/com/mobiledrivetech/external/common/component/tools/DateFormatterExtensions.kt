package com.mobiledrivetech.external.common.component.tools

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter

const val TIME_FORMAT_24_HOURS_MINUTES = "HH:mm"

@RequiresApi(Build.VERSION_CODES.O)
val timeFormat24HoursMinutesFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
    TIME_FORMAT_24_HOURS_MINUTES
)
