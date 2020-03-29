package com.mobilecoronatracker.ui.chart.williamchart.renderer

import com.mobilecoronatracker.ui.chart.williamchart.ChartContract
import com.mobilecoronatracker.ui.chart.williamchart.animation.ChartAnimation
import com.mobilecoronatracker.ui.chart.williamchart.data.DonutChartConfiguration
import com.mobilecoronatracker.ui.chart.williamchart.data.DonutDataPoint
import com.mobilecoronatracker.ui.chart.williamchart.data.Frame
import com.mobilecoronatracker.ui.chart.williamchart.extensions.toDonutDataPoint

class DonutChartRenderer(
    val view: ChartContract.DonutView,
    private var animation: ChartAnimation<DonutDataPoint>
) : ChartContract.DonutRenderer {

    private var innerFrameWithStroke: Frame = Frame(0f, 0f, 0f, 0f)

    private var datapoints = emptyList<DonutDataPoint>()

    private lateinit var chartConfiguration: DonutChartConfiguration

    override fun preDraw(configuration: DonutChartConfiguration): Boolean {

        chartConfiguration = configuration

        if (chartConfiguration.colorsSize < datapoints.size)
            throw IllegalArgumentException(
                "Number of datapoints is ${datapoints.size} " +
                    "but only ${chartConfiguration.colorsSize} color(s) provided."
            )

        val left =
            configuration.paddings.left + configuration.thickness / 2
        val top =
            configuration.paddings.top + configuration.thickness / 2
        val right =
            configuration.width - configuration.paddings.right - configuration.thickness / 2
        val bottom =
            configuration.height - configuration.paddings.bottom - configuration.thickness / 2
        innerFrameWithStroke = Frame(left, top, right, bottom)

        datapoints.forEach { it.screenDegrees = it.value * fullDegrees / chartConfiguration.total }
        datapoints = datapoints.sortedByDescending { it.screenDegrees }

        animation.animateFrom(ignoreStartPosition, datapoints) {
            view.postInvalidate()
        }

        return true
    }

    override fun draw() {

        if (chartConfiguration.barBackgroundColor != -1)
            view.drawBackground(innerFrameWithStroke)

        view.drawArc(datapoints.map { it.screenDegrees }, innerFrameWithStroke)
    }

    override fun render(values: List<Float>) {
        datapoints = values.mapIndexed { index, value ->
            val valueOffset = if (index == 0) 0f else values[index - 1]
            value.toDonutDataPoint(valueOffset)
        }
        view.postInvalidate()
    }

    override fun anim(values: List<Float>, animation: ChartAnimation<DonutDataPoint>) {
        datapoints = values.mapIndexed { index, value ->
            val valueOffset = if (index == 0) 0f else values[index - 1]
            value.toDonutDataPoint(valueOffset)
        }
        this.animation = animation
        view.postInvalidate()
    }

    companion object {
        private const val fullDegrees = 360
        private const val ignoreStartPosition = -1234f
    }
}
