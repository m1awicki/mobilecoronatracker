package com.mobilecoronatracker.ui.chart.williamchart.strategy

import com.mobilecoronatracker.ui.chart.williamchart.ChartContract
import com.mobilecoronatracker.ui.chart.williamchart.Painter
import com.mobilecoronatracker.ui.chart.williamchart.data.Frame
import com.mobilecoronatracker.ui.chart.williamchart.data.Label
import com.mobilecoronatracker.ui.chart.williamchart.renderer.RendererConstants

class DefaultStrategy : ChartContract.HorizontalAxisLabelsPlacingStrategy {
    override fun placeLabels(
        bounds: Frame,
        labelSize: Float,
        painter: Painter,
        labels: List<Label>
    ): List<Label> {
        val labelsLeftPosition = bounds.left +
                painter.measureLabelWidth(labels.first().label, labelSize) / 2
        val labelsRightPosition = bounds.right -
                    painter.measureLabelWidth(labels.last().label, labelSize) / 2
        val widthBetweenLabels = (labelsRightPosition - labelsLeftPosition) / (labels.size - 1)
        val xLabelsVerticalPosition = bounds.bottom -
                    painter.measureLabelAscent(labelSize) + RendererConstants.labelsPaddingToInnerChart

        labels.forEachIndexed { index, label ->
            label.screenPositionX = labelsLeftPosition + (widthBetweenLabels * index)
            label.screenPositionY = xLabelsVerticalPosition
        }
        return labels
    }

}