package com.mobilecoronatracker.ui.chart.williamchart.animation

// Copied from https://github.com/diogobernardino/WilliamChart

import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator

abstract class ChartAnimation<T> {

    var duration: Long = DEFAULT_DURATION
    var interpolator: Interpolator = DecelerateInterpolator()

    abstract fun animateFrom(
        startPosition: Float,
        entries: List<T>,
        callback: () -> Unit
    ): ChartAnimation<T>

    companion object {
        private const val DEFAULT_DURATION = 1000L
    }
}
