package com.mobilecoronatracker.utils

import java.util.Calendar
import java.util.TimeZone

fun getTodayTimestamp(): Long {
    val currentTimeMillis = System.currentTimeMillis()
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = currentTimeMillis
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}