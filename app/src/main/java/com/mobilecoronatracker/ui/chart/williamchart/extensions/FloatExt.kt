package com.mobilecoronatracker.ui.chart.williamchart.extensions

import com.mobilecoronatracker.ui.chart.williamchart.data.DonutDataPoint

fun Float.toDonutDataPoint(offset: Float): DonutDataPoint = DonutDataPoint(this + offset)
