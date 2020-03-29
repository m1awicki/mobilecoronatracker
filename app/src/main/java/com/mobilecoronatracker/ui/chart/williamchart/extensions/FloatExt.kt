package com.mobilecoronatracker.ui.chart.williamchart.extensions

// Copied from https://github.com/diogobernardino/WilliamChart

import com.mobilecoronatracker.ui.chart.williamchart.data.DonutDataPoint

fun Float.toDonutDataPoint(offset: Float): DonutDataPoint = DonutDataPoint(this + offset)
