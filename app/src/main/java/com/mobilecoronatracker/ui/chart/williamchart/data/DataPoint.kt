package com.mobilecoronatracker.ui.chart.williamchart.data

// Copied from https://github.com/diogobernardino/WilliamChart

data class DataPoint(
    val label: String,
    val value: Float,
    var screenPositionX: Float = 0f,
    var screenPositionY: Float = 0f
)
