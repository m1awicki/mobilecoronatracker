package com.mobilecoronatracker.ui.chart.williamchart.animation

import com.mobilecoronatracker.ui.chart.williamchart.data.DataPoint

class NoAnimation : ChartAnimation<DataPoint>() {

    override fun animateFrom(
        startPosition: Float,
        entries: List<DataPoint>,
        callback: () -> Unit
    ): ChartAnimation<DataPoint> = this
}
