package com.mobilecoronatracker.ui.chart.williamchart.extensions

// Copied from https://github.com/diogobernardino/WilliamChart

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

fun Canvas.drawChartBar(bar: RectF, radius: Float, paint: Paint) {
    drawRoundRect(bar, radius, radius, paint)
}
