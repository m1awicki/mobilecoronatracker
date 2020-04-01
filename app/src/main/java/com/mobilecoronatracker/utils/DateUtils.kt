package com.mobilecoronatracker.utils

import java.util.Calendar

fun getTodayTimestamp(): Long = (System.currentTimeMillis() / MILLIS_PER_DAY) * MILLIS_PER_DAY

fun parseDate(date: String, separator: Char = '/'): Calendar {
    val cal = Calendar.getInstance()
    val firstSeparator = date.indexOf(separator, 0)
    val secondSeparator = date.indexOf(separator, firstSeparator + 1)
    val day = date.substring(firstSeparator + 1, secondSeparator).toInt()
    val month = date.substring(0, firstSeparator).toInt()
    var year = date.substring(secondSeparator + 1).toInt()
    if (date.substring(secondSeparator + 1).length == 2) {
        year += 2000
    }
    cal.set(year, month, day)
    cal.timeInMillis = (cal.timeInMillis / MILLIS_PER_DAY) * MILLIS_PER_DAY
    return cal
}

private const val MILLIS_PER_DAY = 1000 * 60 * 60 * 24
