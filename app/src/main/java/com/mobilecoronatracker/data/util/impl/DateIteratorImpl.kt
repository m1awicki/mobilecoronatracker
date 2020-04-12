package com.mobilecoronatracker.data.util.impl

import com.mobilecoronatracker.data.util.DateIterator
import com.mobilecoronatracker.utils.parseDate
import java.util.Calendar

class DateIteratorImpl (startDate: String, private val separator: Char) : DateIterator<String> {
    private val date: Calendar = parseDate(startDate, separator)

    override fun nextDate(): String {
        date.add(Calendar.DATE, 1)
        return date.get(Calendar.MONTH).plus(1).toString() + separator + date.get(Calendar.DAY_OF_MONTH) + separator +
                (date.get(Calendar.YEAR) - 2000)
    }

    override fun prevDate(): String {
        date.add(Calendar.DATE, -1)
        return date.get(Calendar.MONTH).plus(1).toString() + separator + date.get(Calendar.DAY_OF_MONTH) + separator +
                (date.get(Calendar.YEAR) - 2000)
    }
}