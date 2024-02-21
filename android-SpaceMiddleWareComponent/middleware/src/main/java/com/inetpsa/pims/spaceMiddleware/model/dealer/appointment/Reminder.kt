package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment

@Suppress("MagicNumber")
internal enum class Reminder(val timeLong: Long) {

    MIN10(600000),
    MIN20(1200000),
    MIN30(1800000),
    HOUR1(3600000),
    HOUR2(7200000),
    DAY1(86400000),
    DAY2(172800000),
    DAY7(604800000)
}
