package com.mobilecoronatracker.ui.chart.williamchart.extensions

// Copied from https://github.com/diogobernardino/WilliamChart

import android.graphics.drawable.Drawable

fun Drawable.centerAt(x: Float, y: Float) {
    val drawableHalfWidth = this.intrinsicWidth / 2
    val drawableHalfHeight = this.intrinsicHeight / 2
    this.setBounds(
        x.toInt() - drawableHalfWidth,
        y.toInt() - drawableHalfHeight,
        x.toInt() + drawableHalfWidth,
        y.toInt() + drawableHalfHeight
    )
}
