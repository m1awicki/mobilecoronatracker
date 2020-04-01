package com.mobilecoronatracker.data.util

interface DateIterator<T> {
    fun nextDate(): T
    fun prevDate(): T
}