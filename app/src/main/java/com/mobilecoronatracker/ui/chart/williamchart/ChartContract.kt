package com.mobilecoronatracker.ui.chart.williamchart

// Copied from https://github.com/diogobernardino/WilliamChart
// Modified by m1awicki

import com.mobilecoronatracker.ui.chart.williamchart.animation.ChartAnimation
import com.mobilecoronatracker.ui.chart.williamchart.data.ChartConfiguration
import com.mobilecoronatracker.ui.chart.williamchart.data.DataPoint
import com.mobilecoronatracker.ui.chart.williamchart.data.DonutChartConfiguration
import com.mobilecoronatracker.ui.chart.williamchart.data.DonutDataPoint
import com.mobilecoronatracker.ui.chart.williamchart.data.Frame
import com.mobilecoronatracker.ui.chart.williamchart.data.Label

interface ChartContract {

    interface LineView {
        fun postInvalidate()
        fun drawLabels(xLabels: List<Label>)
        fun drawLine(points: List<DataPoint>)
        fun drawLineBackground(innerFrame: Frame, points: List<DataPoint>)
        fun drawPoints(points: List<DataPoint>)
        fun drawDebugFrame(
            outerFrame: Frame,
            innerFrame: Frame,
            labelsFrame: List<Frame>
        )
    }

    interface MultiLineView {
        fun setLineColors(colors: List<Int>)
        fun postInvalidate()
        fun selectColor(colordIndex: Int)
        fun drawLabels(xLabels: List<Label>)
        fun drawLine(points: List<DataPoint>)
        fun drawPoints(points: List<DataPoint>)
        fun drawDebugFrame(
            outerFrame: Frame,
            innerFrame: Frame,
            labelsFrame: List<Frame>
        )
    }

    interface BarView {
        fun postInvalidate()
        fun drawLabels(xLabels: List<Label>)
        fun drawBars(points: List<DataPoint>, innerFrame: Frame)
        fun drawBarsBackground(points: List<DataPoint>, innerFrame: Frame)
        fun drawDebugFrame(
            outerFrame: Frame,
            innerFrame: Frame,
            labelsFrame: List<Frame>
        )
    }

    interface DonutView {
        fun postInvalidate()
        fun drawArc(degrees: List<Float>, innerFrame: Frame)
        fun drawBackground(innerFrame: Frame)
        fun drawDebugFrame(innerFrame: Frame)
    }

    interface Renderer {
        fun preDraw(configuration: ChartConfiguration): Boolean
        fun draw()
        fun render(entries: LinkedHashMap<String, Float>)
        fun anim(entries: LinkedHashMap<String, Float>, animation: ChartAnimation<DataPoint>)
    }

    interface MultiLineRenderer {
        fun preDraw(configuration: ChartConfiguration): Boolean
        fun draw()
        fun render(entries: List<LinkedHashMap<String, Float>>)
        fun anim(labels: List<String>, entries: List<LinkedHashMap<String, Float>>, animation: ChartAnimation<DataPoint>)
    }

    interface DonutRenderer {
        fun preDraw(configuration: DonutChartConfiguration): Boolean
        fun draw()
        fun render(values: List<Float>)
        fun anim(values: List<Float>, animation: ChartAnimation<DonutDataPoint>)
    }

    interface HorizontalAxisLabelsPlacingStrategy {
        fun placeLabels(bounds: Frame, labelSize: Float, painter: Painter, labels: List<Label>): List<Label>
    }
}
