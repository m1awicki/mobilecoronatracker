package com.mobilecoronatracker.ui.chart.williamchart.renderer

import com.mobilecoronatracker.ui.chart.williamchart.ChartContract
import com.mobilecoronatracker.ui.chart.williamchart.Painter
import com.mobilecoronatracker.ui.chart.williamchart.animation.ChartAnimation
import com.mobilecoronatracker.ui.chart.williamchart.data.ChartConfiguration
import com.mobilecoronatracker.ui.chart.williamchart.data.DataPoint
import com.mobilecoronatracker.ui.chart.williamchart.data.Frame
import com.mobilecoronatracker.ui.chart.williamchart.data.Label
import com.mobilecoronatracker.ui.chart.williamchart.data.MultiLineChartConfiguration
import com.mobilecoronatracker.ui.chart.williamchart.data.Scale
import com.mobilecoronatracker.ui.chart.williamchart.data.notInitialized
import com.mobilecoronatracker.ui.chart.williamchart.data.shouldDisplayAxisX
import com.mobilecoronatracker.ui.chart.williamchart.data.shouldDisplayAxisY
import com.mobilecoronatracker.ui.chart.williamchart.data.toOuterFrame
import com.mobilecoronatracker.ui.chart.williamchart.data.withPaddings
import com.mobilecoronatracker.ui.chart.williamchart.extensions.maxValueBy
import com.mobilecoronatracker.ui.chart.williamchart.extensions.toDataPoints
import com.mobilecoronatracker.ui.chart.williamchart.extensions.toScale
import com.mobilecoronatracker.ui.chart.williamchart.renderer.executor.DebugWithLabelsFrame
import com.mobilecoronatracker.ui.chart.williamchart.renderer.executor.MeasureLineChartPaddings
import com.mobilecoronatracker.ui.chart.williamchart.strategy.DefaultStrategy

class MultiLineChartRenderer(
    private val view: ChartContract.MultiLineView,
    private val painter: Painter,
    private var animation: ChartAnimation<DataPoint>,
    private val xLabelsPlacingStrategy: ChartContract.HorizontalAxisLabelsPlacingStrategy = DefaultStrategy()
) : ChartContract.Renderer<List<List<Float>>, List<String>> {
    private var data = emptyList<List<DataPoint>>()

    private lateinit var outerFrame: Frame

    private lateinit var innerFrame: Frame

    private lateinit var chartConfiguration: MultiLineChartConfiguration

    private var xLabels: List<Label> = emptyList()

    private var xLabelsFinal: List<Label> = emptyList()

    private val yLabels by lazy {
        val scaleStep = chartConfiguration.scale.size / RendererConstants.defaultScaleNumberOfSteps

        List(RendererConstants.defaultScaleNumberOfSteps + 1) {
            val scaleValue = chartConfiguration.scale.min + scaleStep * it
            Label(
                label = chartConfiguration.labelsFormatter(scaleValue),
                screenPositionX = 0F,
                screenPositionY = 0F
            )
        }
    }

    override fun preDraw(configuration: ChartConfiguration): Boolean {
        if (data.isEmpty()) return true

        this.chartConfiguration = configuration as MultiLineChartConfiguration

        if (chartConfiguration.scale.notInitialized()) {
            val scales = data.map { it.toScale() }
            val mergedScale = scales.reduce { acc, scale ->
                Scale(
                    if (acc.min < scale.min) acc.min else scale.min,
                    if (acc.max > scale.max) acc.max else scale.max
                )
            }
            chartConfiguration = chartConfiguration.copy(scale = mergedScale)
        }

        val longestChartLabelWidth =
            yLabels.maxValueBy {
                painter.measureLabelWidth(
                    it.label,
                    chartConfiguration.labelsSize
                )
            }
                ?: throw IllegalArgumentException("Looks like there's no labels to find the longest width.")

        val paddings = MeasureLineChartPaddings()(
            axisType = chartConfiguration.axis,
            labelsHeight = painter.measureLabelHeight(chartConfiguration.labelsSize),
            longestLabelWidth = longestChartLabelWidth,
            labelsPaddingToInnerChart = RendererConstants.labelsPaddingToInnerChart,
            lineThickness = chartConfiguration.lineThickness,
            pointsDrawableWidth = chartConfiguration.pointsDrawableWidth,
            pointsDrawableHeight = chartConfiguration.pointsDrawableHeight
        )

        outerFrame = chartConfiguration.toOuterFrame()
        innerFrame = outerFrame.withPaddings(paddings)

        if (chartConfiguration.axis.shouldDisplayAxisX())
            placeLabelsX(innerFrame)

        if (chartConfiguration.axis.shouldDisplayAxisY())
            placeLabelsY(innerFrame)

        placeDataPoints(innerFrame)

        data.forEach {
            animation.animateFrom(innerFrame.bottom, it) { view.postInvalidate() }
        }

        return false
    }

    override fun draw() {
        if (data.isEmpty()) return

        if (chartConfiguration.axis.shouldDisplayAxisX())
            view.drawLabels(xLabelsFinal)

        if (chartConfiguration.axis.shouldDisplayAxisY())
            view.drawLabels(yLabels)

        var colorIndex = 0
        data.forEach {
            view.selectColor(colorIndex)
            view.drawLine(it)
            view.drawPoints(it)
            colorIndex++
        }

        if (RendererConstants.inDebug) {
            view.drawDebugFrame(
                outerFrame,
                innerFrame,
                DebugWithLabelsFrame()(
                    painter = painter,
                    axisType = chartConfiguration.axis,
                    xLabels = xLabelsFinal,
                    yLabels = yLabels,
                    labelsSize = chartConfiguration.labelsSize
                )
            )
        }
    }

    override fun render(labels: List<String>, entries: List<List<Float>>) {
        data = entries.map { it.toDataPoints() }
        xLabels = labels.map { Label(it, 0f, 0f) }
        view.postInvalidate()
    }

    override fun anim(
        labels: List<String>,
        entries: List<List<Float>>,
        animation: ChartAnimation<DataPoint>
    ) {
        data = entries.map { it.toDataPoints() }
        xLabels = labels.map { Label(it, 0f, 0f) }
        this.animation = animation
        view.postInvalidate()
    }

    private fun placeLabelsX(innerFrame: Frame) {
        xLabelsFinal = xLabelsPlacingStrategy.placeLabels(
            innerFrame, chartConfiguration.labelsSize, painter, xLabels
        )
    }

    private fun placeLabelsY(innerFrame: Frame) {
        val heightBetweenLabels =
            (innerFrame.bottom - innerFrame.top) / RendererConstants.defaultScaleNumberOfSteps
        val labelsBottomPosition =
            innerFrame.bottom + painter.measureLabelHeight(chartConfiguration.labelsSize) / 2

        yLabels.forEachIndexed { index, label ->
            label.screenPositionX =
                innerFrame.left -
                        RendererConstants.labelsPaddingToInnerChart -
                        painter.measureLabelWidth(label.label, chartConfiguration.labelsSize) / 2
            label.screenPositionY = labelsBottomPosition - heightBetweenLabels * index
        }
    }

    private fun placeDataPoints(innerFrame: Frame) {
        val scaleSize = chartConfiguration.scale.size
        val chartHeight = innerFrame.bottom - innerFrame.top
        val maxLen = data.maxValueBy { it.size } ?: 1
        val widthBetweenLabels = (innerFrame.right - innerFrame.left) / (maxLen.minus(1))

        data.forEach {
            it.forEachIndexed { index, dataPoint ->
                dataPoint.screenPositionX = innerFrame.left + (widthBetweenLabels * index)
                dataPoint.screenPositionY = innerFrame.bottom -
                        (chartHeight * (dataPoint.value - chartConfiguration.scale.min) / scaleSize)
            }
        }
    }
}