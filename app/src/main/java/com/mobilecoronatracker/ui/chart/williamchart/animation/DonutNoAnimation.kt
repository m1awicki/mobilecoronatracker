package com.mobilecoronatracker.ui.chart.williamchart.animation

// Copied from https://github.com/diogobernardino/WilliamChart

import com.mobilecoronatracker.ui.chart.williamchart.data.DonutDataPoint

class DonutNoAnimation : ChartAnimation<DonutDataPoint>() {

    override fun animateFrom(
        startPosition: Float,
        entries: List<DonutDataPoint>,
        callback: () -> Unit
    ): ChartAnimation<DonutDataPoint> = this
}
