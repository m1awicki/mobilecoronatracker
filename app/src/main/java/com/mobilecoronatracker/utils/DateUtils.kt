package com.mobilecoronatracker.utils

fun getTodayTimestamp(): Long = (System.currentTimeMillis() / MILLIS_PER_DAY) * MILLIS_PER_DAY

private const val MILLIS_PER_DAY = 1000 * 60 * 60 * 24
