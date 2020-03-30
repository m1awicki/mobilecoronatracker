package com.mobilecoronatracker.ui.chart.williamchart.view

// Copied from https://github.com/diogobernardino/WilliamChart

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.ColorInt
import com.mobilecoronatracker.R
import com.mobilecoronatracker.ui.chart.williamchart.ChartContract.*
import com.mobilecoronatracker.ui.chart.williamchart.animation.NoAnimation
import com.mobilecoronatracker.ui.chart.williamchart.data.*
import com.mobilecoronatracker.ui.chart.williamchart.extensions.drawChartBar
import com.mobilecoronatracker.ui.chart.williamchart.extensions.obtainStyledAttributes
import com.mobilecoronatracker.ui.chart.williamchart.renderer.BarChartRenderer
import com.mobilecoronatracker.ui.chart.williamchart.strategy.BarChartStrategy

class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AxisChartView<LinkedHashMap<String, Float>, List<String>>(
    context,
    attrs,
    defStyleAttr
), BarView {

    @Suppress("MemberVisibilityCanBePrivate")
    var spacing = defaultSpacing

    @ColorInt
    @Suppress("MemberVisibilityCanBePrivate")
    var barsColor: Int = defaultBarsColor

    @Suppress("MemberVisibilityCanBePrivate")
    var barRadius: Float = defaultBarsRadius

    @Suppress("MemberVisibilityCanBePrivate")
    var barsBackgroundColor: Int = -1

    override val chartConfiguration: ChartConfiguration
        get() =
            BarChartConfiguration(
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
                scale = scale,
                barsBackgroundColor = barsBackgroundColor,
                labelsFormatter = labelsFormatter
            )

    override fun getDefaultHorizontalLabelsStrategy(): HorizontalAxisLabelsPlacingStrategy {
        return defaultBarChartHorizontalLabelsStrategy
    }

    init {
        handleAttributes(obtainStyledAttributes(attrs, R.styleable.BarChartAttrs))
        renderer = BarChartRenderer(
            this,
            painter,
            NoAnimation(),
            defaultBarChartHorizontalLabelsStrategy
        )
        handleEditMode()
    }

    override fun drawBars(
        points: List<DataPoint>,
        innerFrame: Frame
    ) {

        val halfBarWidth =
            (innerFrame.right - innerFrame.left - (points.size + 1) * spacing) / points.size / 2

        painter.prepare(color = barsColor, style = Paint.Style.FILL)
        points.forEach {
            val bar = RectF(
                it.screenPositionX - halfBarWidth,
                it.screenPositionY,
                it.screenPositionX + halfBarWidth,
                innerFrame.bottom
            )
            canvas.drawChartBar(bar, barRadius, painter.paint)
        }
    }

    override fun drawBarsBackground(points: List<DataPoint>, innerFrame: Frame) {

        val halfBarWidth =
            (innerFrame.right - innerFrame.left - (points.size + 1) * spacing) / points.size / 2

        painter.prepare(color = barsBackgroundColor, style = Paint.Style.FILL)
        points.forEach {
            val bar = RectF(
                it.screenPositionX - halfBarWidth,
                innerFrame.top,
                it.screenPositionX + halfBarWidth,
                innerFrame.bottom
            )
            canvas.drawChartBar(bar, barRadius, painter.paint)
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

    override fun drawDebugFrame(outerFrame: Frame, innerFrame: Frame, labelsFrame: List<Frame>) {
        painter.prepare(color = -0x1000000, style = Paint.Style.STROKE)
        canvas.drawRect(outerFrame.toRect(), painter.paint)
        canvas.drawRect(innerFrame.toRect(), painter.paint)
        labelsFrame.forEach { canvas.drawRect(it.toRect(), painter.paint) }
    }

    private fun handleAttributes(typedArray: TypedArray) {
        typedArray.apply {
            spacing = getDimension(R.styleable.BarChartAttrs_chart_spacing, spacing)
            barsColor = getColor(R.styleable.BarChartAttrs_chart_barsColor, barsColor)
            barRadius = getDimension(R.styleable.BarChartAttrs_chart_barsRadius, barRadius)
            barsBackgroundColor =
                getColor(R.styleable.BarChartAttrs_chart_barsBackgroundColor, barsBackgroundColor)
            recycle()
        }
    }

    companion object {
        private const val defaultSpacing = 10f
        private const val defaultBarsColor = Color.BLACK
        private const val defaultBarsRadius = 0F
        private val defaultBarChartHorizontalLabelsStrategy:
                HorizontalAxisLabelsPlacingStrategy = BarChartStrategy()
    }

    override fun getEditModeData(): LinkedHashMap<String, Float> = AxisChartData.editModeSampleData
    override fun getEditModeLabels(): List<String> = AxisChartData.editModeSampleData.map { it.key }
}
