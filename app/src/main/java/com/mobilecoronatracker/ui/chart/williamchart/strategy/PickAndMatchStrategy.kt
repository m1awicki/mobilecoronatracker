package com.mobilecoronatracker.ui.chart.williamchart.strategy

import com.mobilecoronatracker.ui.chart.williamchart.ChartContract
import com.mobilecoronatracker.ui.chart.williamchart.Painter
import com.mobilecoronatracker.ui.chart.williamchart.data.Frame
import com.mobilecoronatracker.ui.chart.williamchart.data.Label
import com.mobilecoronatracker.ui.chart.williamchart.renderer.RendererConstants
import kotlin.math.roundToInt

class PickAndMatchStrategy : ChartContract.HorizontalAxisLabelsPlacingStrategy {
    override fun placeLabels(
        bounds: Frame,
        labelSize: Float,
        painter: Painter,
        labels: List<Label>
    ): List<Label> {
        val averageLabelWidth: Float = labels.map {
            painter.measureLabelWidth(
                it.label, labelSize)
        }.reduce { acc, fl ->
            acc + fl
        }.div(labels.size.toFloat())
        val labelsLeftPosition = bounds.left +
                    painter.measureLabelWidth(labels.first().label, labelSize) / 2
        val labelsRightPosition = bounds.right -
                    painter.measureLabelWidth(labels.last().label, labelSize) / 2
        val totalLabelsWidth = labelsRightPosition - labelsLeftPosition
        val labelsFitCount = (totalLabelsWidth / averageLabelWidth).roundToInt()
        val remIdx = (labels.size.toFloat() / (labels.size - labelsFitCount)).toInt()
        val selIdx = (labels.size.toFloat() / labelsFitCount).roundToInt()
        val xLabelsVerticalPosition = bounds.bottom -
                    painter.measureLabelAscent(labelSize) + RendererConstants.labelsPaddingToInnerChart

        val labelsFinal = if (remIdx > 0 && selIdx > 0) {
            labels.filterIndexed { index, _ ->
                if (remIdx >= 2)
                    index % remIdx != 0 || index == 0
                else
                    index % selIdx == 0
            }
        } else {
            labels
        }
        val labelOffset = totalLabelsWidth / (labelsFinal.size - 1)
        labelsFinal.forEachIndexed { index, label ->
            label.screenPositionX = labelsLeftPosition + (labelOffset * index)
            label.screenPositionY = xLabelsVerticalPosition
        }
        return labelsFinal
    }
}