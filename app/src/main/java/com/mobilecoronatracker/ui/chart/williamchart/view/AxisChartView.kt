package com.mobilecoronatracker.ui.chart.williamchart.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnPreDraw
import com.mobilecoronatracker.R
import com.mobilecoronatracker.ui.chart.williamchart.ChartContract
import com.mobilecoronatracker.ui.chart.williamchart.Painter
import com.mobilecoronatracker.ui.chart.williamchart.animation.ChartAnimation
import com.mobilecoronatracker.ui.chart.williamchart.animation.DefaultAnimation
import com.mobilecoronatracker.ui.chart.williamchart.data.AxisType
import com.mobilecoronatracker.ui.chart.williamchart.data.ChartConfiguration
import com.mobilecoronatracker.ui.chart.williamchart.data.DataPoint
import com.mobilecoronatracker.ui.chart.williamchart.data.Scale
import com.mobilecoronatracker.ui.chart.williamchart.extensions.obtainStyledAttributes
import com.mobilecoronatracker.ui.chart.williamchart.renderer.RendererConstants.Companion.notInitialized

abstract class AxisChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var labelsSize: Float = defaultLabelsSize

    var labelsColor: Int = -0x1000000

    var labelsFont: Typeface? = null

    var axis: AxisType = AxisType.XY

    var scale: Scale = Scale(notInitialized, notInitialized)

    var labelsFormatter: (Float) -> String = { it.toString() }

    var animation: ChartAnimation<DataPoint> = DefaultAnimation()

    protected lateinit var canvas: Canvas

    protected val painter: Painter = Painter(labelsFont = labelsFont)

    // Initialized in init() by chart views extending `AxisChartView` (e.g. LineChartView)
    protected lateinit var renderer: ChartContract.Renderer

    init {
        handleAttributes(obtainStyledAttributes(attrs, R.styleable.ChartAttrs))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.setWillNotDraw(false)
        // style.init()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // style.clean()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        setMeasuredDimension(
            if (widthMode == MeasureSpec.AT_MOST) defaultFrameWidth else widthMeasureSpec,
            if (heightMode == MeasureSpec.AT_MOST) defaultFrameHeight else heightMeasureSpec
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        renderer.draw()
    }

    abstract val chartConfiguration: ChartConfiguration

    fun show(entries: LinkedHashMap<String, Float>) {
        doOnPreDraw { renderer.preDraw(chartConfiguration) }
        renderer.render(entries)
    }

    fun animate(entries: LinkedHashMap<String, Float>) {
        doOnPreDraw { renderer.preDraw(chartConfiguration) }
        renderer.anim(entries, animation)
    }

    private fun handleAttributes(typedArray: TypedArray) {
        typedArray.apply {

            axis = when (getString(R.styleable.ChartAttrs_chart_axis)) {
                "0" -> AxisType.NONE
                "1" -> AxisType.X
                "2" -> AxisType.Y
                else -> AxisType.XY
            }

            labelsSize = getDimension(R.styleable.ChartAttrs_chart_labelsSize, labelsSize)

            labelsColor = getColor(R.styleable.ChartAttrs_chart_labelsColor, labelsColor)

            if (hasValue(R.styleable.ChartAttrs_chart_labelsFont) && !isInEditMode) {
                labelsFont =
                    ResourcesCompat.getFont(
                        context,
                        getResourceId(R.styleable.ChartAttrs_chart_labelsFont, -1)
                    )
                painter.labelsFont = labelsFont
            }

            recycle()
        }
    }

    protected fun handleEditMode() {
        if (isInEditMode) {
            show(editModeSampleData)
        }
    }

    companion object {
        private const val defaultFrameWidth = 200
        private const val defaultFrameHeight = 100
        private const val defaultLabelsSize = 60F
        private val editModeSampleData =
            linkedMapOf(
                "Label1" to 1f,
                "Label2" to 7.5f,
                "Label3" to 4.7f,
                "Label4" to 3.5f
            )
    }
}
