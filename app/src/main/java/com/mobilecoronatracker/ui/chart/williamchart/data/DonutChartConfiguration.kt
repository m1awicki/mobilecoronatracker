package com.mobilecoronatracker.ui.chart.williamchart.data

// Copied from https://github.com/diogobernardino/WilliamChart

data class DonutChartConfiguration(
    val width: Int,
    val height: Int,
    val paddings: Paddings,
    val thickness: Float,
    val total: Float,
    val colorsSize: Int,
    val barBackgroundColor: Int
)
