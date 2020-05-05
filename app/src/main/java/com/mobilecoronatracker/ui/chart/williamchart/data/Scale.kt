package com.mobilecoronatracker.ui.chart.williamchart.data

// Copied from https://github.com/diogobernardino/WilliamChart

import com.mobilecoronatracker.ui.chart.williamchart.renderer.RendererConstants.Companion.notInitialized

data class Scale(val min: Float, val max: Float) {
    val size = max - min
}

fun Scale.notInitialized() = max == min && min == notInitialized
