package com.mobilecoronatracker.ui.chart.williamchart.renderer.executor

import com.mobilecoronatracker.ui.chart.williamchart.Painter
import com.mobilecoronatracker.ui.chart.williamchart.data.AxisType
import com.mobilecoronatracker.ui.chart.williamchart.data.Frame
import com.mobilecoronatracker.ui.chart.williamchart.data.Label
import com.mobilecoronatracker.ui.chart.williamchart.data.shouldDisplayAxisX
import com.mobilecoronatracker.ui.chart.williamchart.data.shouldDisplayAxisY

class DebugWithLabelsFrame {

    operator fun invoke(
        painter: Painter,
        axisType: AxisType,
        xLabels: List<Label>,
        yLabels: List<Label>,
        labelsSize: Float
    ): List<Frame> {
        val ascent = painter.measureLabelAscent(labelsSize)
        val descent = painter.measureLabelDescent(labelsSize)

        val labelsFrames = mutableListOf<Frame>()

        if (axisType.shouldDisplayAxisX())
            labelsFrames += xLabels.map {
                val labelHalfWidth = painter.measureLabelWidth(it.label, labelsSize) / 2
                Frame(
                    left = it.screenPositionX - labelHalfWidth,
                    top = it.screenPositionY + ascent,
                    right = it.screenPositionX + labelHalfWidth,
                    bottom = it.screenPositionY + descent
                )
            }

        if (axisType.shouldDisplayAxisY())
            labelsFrames += yLabels.map {
                val labelHalfWidth = painter.measureLabelWidth(it.label, labelsSize) / 2
                Frame(
                    left = it.screenPositionX - labelHalfWidth,
                    top = it.screenPositionY + ascent,
                    right = it.screenPositionX + labelHalfWidth,
                    bottom = it.screenPositionY + descent
                )
            }
        return labelsFrames.toList()
    }
}
