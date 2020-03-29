package com.mobilecoronatracker.ui.chart.williamchart.animation

import com.mobilecoronatracker.ui.chart.williamchart.data.DonutDataPoint

class DonutNoAnimation : ChartAnimation<DonutDataPoint>() {

    override fun animateFrom(
        startPosition: Float,
        entries: List<DonutDataPoint>,
        callback: () -> Unit
    ): ChartAnimation<DonutDataPoint> = this
}
