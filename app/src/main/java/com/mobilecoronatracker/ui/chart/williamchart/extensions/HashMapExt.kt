package com.mobilecoronatracker.ui.chart.williamchart.extensions

// Copied from https://github.com/diogobernardino/WilliamChart

import com.mobilecoronatracker.ui.chart.williamchart.data.DataPoint

internal fun HashMap<String, Float>.toDataPoints(): List<DataPoint> {
    return entries.map {
        DataPoint(
            label = it.key,
            value = it.value,
            screenPositionX = 0f,
            screenPositionY = 0f
        )
    }
}
