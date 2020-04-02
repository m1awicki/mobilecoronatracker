package com.mobilecoronatracker.ui.chart.williamchart.renderer

// Copied from https://github.com/diogobernardino/WilliamChart

import com.mobilecoronatracker.ui.chart.williamchart.ChartContract
import com.mobilecoronatracker.ui.chart.williamchart.Painter
import com.mobilecoronatracker.ui.chart.williamchart.animation.ChartAnimation
import com.mobilecoronatracker.ui.chart.williamchart.data.BarChartConfiguration
import com.mobilecoronatracker.ui.chart.williamchart.data.ChartConfiguration
import com.mobilecoronatracker.ui.chart.williamchart.data.DataPoint
import com.mobilecoronatracker.ui.chart.williamchart.data.Frame
import com.mobilecoronatracker.ui.chart.williamchart.data.Label
import com.mobilecoronatracker.ui.chart.williamchart.data.Scale
import com.mobilecoronatracker.ui.chart.williamchart.data.notInitialized
import com.mobilecoronatracker.ui.chart.williamchart.data.shouldDisplayAxisX
import com.mobilecoronatracker.ui.chart.williamchart.data.shouldDisplayAxisY
import com.mobilecoronatracker.ui.chart.williamchart.data.toOuterFrame
import com.mobilecoronatracker.ui.chart.williamchart.data.withPaddings
import com.mobilecoronatracker.ui.chart.williamchart.extensions.limits
import com.mobilecoronatracker.ui.chart.williamchart.extensions.maxValueBy
import com.mobilecoronatracker.ui.chart.williamchart.extensions.toDataPoints
import com.mobilecoronatracker.ui.chart.williamchart.renderer.executor.DebugWithLabelsFrame
import com.mobilecoronatracker.ui.chart.williamchart.renderer.executor.MeasureBarChartPaddings
import com.mobilecoronatracker.ui.chart.williamchart.strategy.BarChartStrategy
import kotlin.math.max

class BarChartRenderer(
    private val view: ChartContract.BarView,
    private val painter: Painter,
    private var animation: ChartAnimation<DataPoint>,
    private val xLabelsPlacingStrategy: ChartContract.HorizontalAxisLabelsPlacingStrategy = BarChartStrategy()
)  : ChartContract.Renderer<List<Float>, List<String>> {

    private var data = emptyList<DataPoint>()

    private lateinit var outerFrame: Frame

    private lateinit var innerFrame: Frame

    private lateinit var chartConfiguration: BarChartConfiguration

    private var xLabels: List<Label> = emptyList()

    private var xLabelsText: List<String> = emptyList()

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

        chartConfiguration = configuration as BarChartConfiguration

        if (chartConfiguration.scale.notInitialized())
            chartConfiguration =
                chartConfiguration.copy(
                    scale = Scale(
                        min = 0F,
                        max = data.limits().second
                    )
                )

        val longestChartLabelWidth =
            yLabels.maxValueBy {
                painter.measureLabelWidth(
                    it.label,
                    chartConfiguration.labelsSize
                )
            }
                ?: throw IllegalArgumentException("Looks like there's no labels to find the longest width.")

        val paddings = MeasureBarChartPaddings()(
            axisType = chartConfiguration.axis,
            labelsHeight = painter.measureLabelHeight(chartConfiguration.labelsSize),
            longestLabelWidth = longestChartLabelWidth,
            labelsPaddingToInnerChart = RendererConstants.labelsPaddingToInnerChart
        )

        outerFrame = chartConfiguration.toOuterFrame()
        innerFrame = outerFrame.withPaddings(paddings)

        if (chartConfiguration.axis.shouldDisplayAxisX())
            placeLabelsX(innerFrame)

        if (chartConfiguration.axis.shouldDisplayAxisY())
            placeLabelsY(innerFrame)

        placeDataPoints(innerFrame)

        animation.animateFrom(innerFrame.bottom, data) { view.postInvalidate() }

        return false
    }

    override fun draw() {
        if (data.isEmpty()) return

        if (chartConfiguration.axis.shouldDisplayAxisX())
            view.drawLabels(xLabels)

        if (chartConfiguration.axis.shouldDisplayAxisY())
            view.drawLabels(yLabels)

        if (chartConfiguration.barsBackgroundColor != -1)
            view.drawBarsBackground(data, innerFrame)

        view.drawBars(data, innerFrame)

        if (RendererConstants.inDebug) {
            view.drawDebugFrame(
                outerFrame,
                innerFrame,
                DebugWithLabelsFrame()(
                    painter = painter,
                    axisType = chartConfiguration.axis,
                    xLabels = xLabels,
                    yLabels = yLabels,
                    labelsSize = chartConfiguration.labelsSize
                )
            )
        }
    }

    override fun render(labels: List<String>, entries: List<Float>) {
        data = entries.toDataPoints()
        xLabelsText = labels
        view.postInvalidate()
    }

    override fun anim(
        labels: List<String>,
        entries: List<Float>,
        animation: ChartAnimation<DataPoint>
    ) {
        data = entries.toDataPoints()
        xLabelsText = labels
        this.animation = animation
        view.postInvalidate()
    }

    private fun placeLabelsX(innerFrame: Frame) {
        xLabels = xLabelsPlacingStrategy.placeLabels(innerFrame, chartConfiguration.labelsSize,
            painter, xLabelsText.map { Label(it, 0f, 0f) })
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
        val halfBarWidth = (innerFrame.right - innerFrame.left) / xLabels.size / 2
        val labelsLeftPosition = innerFrame.left + halfBarWidth
        val labelsRightPosition = innerFrame.right - halfBarWidth
        val widthBetweenLabels = (labelsRightPosition - labelsLeftPosition) / (xLabels.size - 1)
        data.forEachIndexed { index, dataPoint ->
            dataPoint.screenPositionX = labelsLeftPosition + (widthBetweenLabels * index)
            dataPoint.screenPositionY =
                innerFrame.bottom -
                    // bar length must be positive, or zero
                    (chartHeight * max(
                        0f,
                        dataPoint.value - chartConfiguration.scale.min
                    ) / scaleSize)
        }
    }
}
