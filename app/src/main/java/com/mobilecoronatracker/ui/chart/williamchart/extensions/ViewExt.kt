package com.mobilecoronatracker.ui.chart.williamchart.extensions

// Copied from https://github.com/diogobernardino/WilliamChart

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat

fun View.getDrawable(drawableRes: Int): Drawable? =
    ContextCompat.getDrawable(this.context, drawableRes)
