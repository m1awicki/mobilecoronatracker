package com.mobilecoronatracker.ui.chart.williamchart.renderer.executor

import com.mobilecoronatracker.ui.chart.williamchart.data.AxisType
import com.mobilecoronatracker.ui.chart.williamchart.data.Paddings
import com.mobilecoronatracker.ui.chart.williamchart.data.mergeWith
import com.mobilecoronatracker.ui.chart.williamchart.data.shouldDisplayAxisX
import com.mobilecoronatracker.ui.chart.williamchart.data.shouldDisplayAxisY

class MeasureHorizontalBarChartPaddings {

    operator fun invoke(
        axisType: AxisType,
        labelsHeight: Float,
        xLastLabelWidth: Float,
        yLongestLabelWidth: Float,
        labelsPaddingToInnerChart: Float
    ): Paddings {

        return measurePaddingsX(axisType, labelsHeight, xLastLabelWidth, labelsPaddingToInnerChart)
            .mergeWith(
                measurePaddingsY(axisType, yLongestLabelWidth, labelsPaddingToInnerChart)
            )
    }

    private fun measurePaddingsX(
        axisType: AxisType,
        labelsHeight: Float,
        xLastLabelWidth: Float,
        labelsPaddingToInnerChart: Float
    ): Paddings {

        if (!axisType.shouldDisplayAxisX())
            return Paddings(0F, 0F, 0F, 0F)

        return Paddings(
            left = 0F,
            top = 0F,
            right = xLastLabelWidth / 2,
            bottom = labelsHeight + labelsPaddingToInnerChart
        )
    }

    private fun measurePaddingsY(
        axisType: AxisType,
        yLongestLabelWidth: Float,
        labelsPaddingToInnerChart: Float
    ): Paddings {

        if (!axisType.shouldDisplayAxisY())
            return Paddings(0F, 0F, 0F, 0F)

        return Paddings(
            left = yLongestLabelWidth + labelsPaddingToInnerChart,
            top = 0f,
            right = 0F,
            bottom = 0f
        )
    }
}
