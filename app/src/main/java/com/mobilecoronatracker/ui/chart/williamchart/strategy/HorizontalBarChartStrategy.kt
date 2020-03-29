package com.mobilecoronatracker.ui.chart.williamchart.strategy

import com.mobilecoronatracker.ui.chart.williamchart.ChartContract
import com.mobilecoronatracker.ui.chart.williamchart.Painter
import com.mobilecoronatracker.ui.chart.williamchart.data.Frame
import com.mobilecoronatracker.ui.chart.williamchart.data.Label
import com.mobilecoronatracker.ui.chart.williamchart.renderer.RendererConstants

class HorizontalBarChartStrategy : ChartContract.HorizontalAxisLabelsPlacingStrategy {
    override fun placeLabels(
        bounds: Frame,
        labelSize: Float,
        painter: Painter,
        labels: List<Label>
    ): List<Label> {
        val widthBetweenLabels =
            (bounds.right - bounds.left) / RendererConstants.defaultScaleNumberOfSteps
        val xLabelsVerticalPosition = bounds.bottom - painter.measureLabelAscent(labelSize) +
                    RendererConstants.labelsPaddingToInnerChart
        labels.forEachIndexed { index, label ->
            label.screenPositionX = bounds.left + widthBetweenLabels * index
            label.screenPositionY = xLabelsVerticalPosition
        }
        return labels
    }
}