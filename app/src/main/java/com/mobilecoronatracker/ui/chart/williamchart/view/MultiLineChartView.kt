package com.mobilecoronatracker.ui.chart.williamchart.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import com.mobilecoronatracker.ui.chart.williamchart.ChartContract
import com.mobilecoronatracker.R
import com.mobilecoronatracker.ui.chart.williamchart.animation.NoAnimation
import com.mobilecoronatracker.ui.chart.williamchart.data.AxisChartData
import com.mobilecoronatracker.ui.chart.williamchart.data.ChartConfiguration
import com.mobilecoronatracker.ui.chart.williamchart.data.DataPoint
import com.mobilecoronatracker.ui.chart.williamchart.data.Frame
import com.mobilecoronatracker.ui.chart.williamchart.data.Label
import com.mobilecoronatracker.ui.chart.williamchart.data.MultiLineChartConfiguration
import com.mobilecoronatracker.ui.chart.williamchart.data.Paddings
import com.mobilecoronatracker.ui.chart.williamchart.data.toRect
import com.mobilecoronatracker.ui.chart.williamchart.extensions.centerAt
import com.mobilecoronatracker.ui.chart.williamchart.extensions.getDrawable
import com.mobilecoronatracker.ui.chart.williamchart.extensions.obtainStyledAttributes
import com.mobilecoronatracker.ui.chart.williamchart.extensions.toLinePath
import com.mobilecoronatracker.ui.chart.williamchart.extensions.toSmoothLinePath
import com.mobilecoronatracker.ui.chart.williamchart.renderer.MultiLineChartRenderer
import com.mobilecoronatracker.ui.chart.williamchart.strategy.DefaultStrategy

class MultiLineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AxisChartView<List<LinkedHashMap<String, Float>>, List<String>>(
    context,
    attrs,
    defStyleAttr
), ChartContract.MultiLineView {

    @Suppress("MemberVisibilityCanBePrivate")
    var smooth: Boolean = defaultSmooth

    @Suppress("MemberVisibilityCanBePrivate")
    var lineThickness: Float = defaultLineThickness

    @Suppress("MemberVisibilityCanBePrivate")
    var lineColor: Int = defaultLineColor

    private var colors : List<Int> = emptyList()
    private var currColor = 0

    @DrawableRes
    @Suppress("MemberVisibilityCanBePrivate")
    var pointsDrawableRes = -1

    override val chartConfiguration: ChartConfiguration
        get() =
            MultiLineChartConfiguration(
                    width = measuredWidth,
                    height = measuredHeight,
                    paddings = Paddings(
                            paddingLeft.toFloat(),
                            paddingTop.toFloat(),
                            paddingRight.toFloat(),
                            paddingBottom.toFloat()
                    ),
                    axis = axis,
                    labelsSize = labelsSize,
                    lineThickness = lineThickness,
                    scale = scale,
                    pointsDrawableWidth = if (pointsDrawableRes != -1)
                        getDrawable(pointsDrawableRes)!!.intrinsicWidth else -1,
                    pointsDrawableHeight = if (pointsDrawableRes != -1)
                        getDrawable(pointsDrawableRes)!!.intrinsicHeight else -1,
                    labelsFormatter = labelsFormatter
            )

    override fun getDefaultHorizontalLabelsStrategy(): ChartContract.HorizontalAxisLabelsPlacingStrategy {
        return defaultLineChartHorizontalLabelsStrategy
    }

    init {
        handleAttributes(obtainStyledAttributes(attrs, R.styleable.LineChartAttrs))
        renderer = MultiLineChartRenderer(this, painter, NoAnimation(), horizontalLabelsStrategy)
        handleEditMode()
    }

    override fun drawLine(points: List<DataPoint>) {
        val linePath =
                if (!smooth) points.toLinePath()
                else points.toSmoothLinePath(defaultSmoothFactor)

        painter.prepare(color = currColor, style = Paint.Style.STROKE, strokeWidth = lineThickness)
        canvas.drawPath(linePath, painter.paint)
    }

    override fun setLineColors(colors: List<Int>) {
        this.colors = colors
    }

    override fun selectColor(colordIndex: Int) {
        currColor = when (colors.size) {
            0 -> lineColor
            else -> if (colordIndex < colors.size) {
                colors [colordIndex]
            } else {
                colors[colors.size-1]
            }
        }
    }

    override fun drawLabels(xLabels: List<Label>) {
        painter.prepare(
                textSize = labelsSize,
                color = labelsColor,
                font = labelsFont
        )
        xLabels.forEach {
            canvas.drawText(
                    it.label,
                    it.screenPositionX,
                    it.screenPositionY,
                    painter.paint
            )
        }
    }

    override fun drawPoints(points: List<DataPoint>) {
        if (pointsDrawableRes != -1) {
            points.forEach { dataPoint ->
                getDrawable(pointsDrawableRes)?.let {
                    it.centerAt(dataPoint.screenPositionX, dataPoint.screenPositionY)
                    it.colorFilter = PorterDuffColorFilter(currColor, PorterDuff.Mode.SRC_IN)
                    it.draw(canvas)
                }
            }
        }
    }

    override fun drawDebugFrame(outerFrame: Frame, innerFrame: Frame, labelsFrame: List<Frame>) {
        painter.prepare(color = -0x1000000, style = Paint.Style.STROKE)
        canvas.drawRect(outerFrame.toRect(), painter.paint)
        canvas.drawRect(innerFrame.toRect(), painter.paint)
        labelsFrame.forEach { canvas.drawRect(it.toRect(), painter.paint) }
    }

    private fun handleAttributes(typedArray: TypedArray) {
        typedArray.apply {
            lineColor = getColor(R.styleable.LineChartAttrs_chart_lineColor, lineColor)
            lineThickness =
                    getDimension(R.styleable.LineChartAttrs_chart_lineThickness, lineThickness)
            smooth = getBoolean(R.styleable.LineChartAttrs_chart_smoothLine, smooth)
            pointsDrawableRes =
                    getResourceId(R.styleable.LineChartAttrs_chart_pointsDrawable, pointsDrawableRes)
            recycle()
        }
    }

    override fun getEditModeData(): List<LinkedHashMap<String, Float>> = AxisChartData.editModeMultiLineSampleData
    override fun getEditModeLabels(): List<String> = AxisChartData.editModeMultiLineSampleDataLabels

    companion object {
        private const val defaultSmoothFactor = 0.20f
        private const val defaultSmooth = false
        private const val defaultLineThickness = 4F
        private const val defaultLineColor = Color.BLACK
        private val defaultLineChartHorizontalLabelsStrategy = DefaultStrategy()
    }
}