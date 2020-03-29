package com.mobilecoronatracker.ui.charts_test

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobilecoronatracker.R
import com.mobilecoronatracker.ui.chart.williamchart.data.AxisType
import kotlinx.android.synthetic.main.fragment_charts_test.*

class FragmentChartsTest : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_charts_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        multiLineChart.axis = AxisType.X
        multiLineChart.labelsFormatter = { data -> data.toInt().toString() }
        multiLineChart.animation.duration = animationDuration
        multiLineChart.setLineColors(listOf(
            Color.RED,
            Color.BLUE,
            Color.DKGRAY,
            Color.GREEN
        ))
        multiLineChart.animate(multiLineLabels, multiLineSet)
    }

    companion object {
        private val multiLineLabels = listOf(
            "Feb 01", "Feb 02", "Feb 04", "Feb 10", "Feb 12", "Feb 14", "Feb 20", "Feb 23",
            "Feb 27", "Mar 01", "Mar 04", "Mar 06", "Mar 08", "Mar 10", "Mar 12", "Mar 14",
            "Mar 16","Mar 18","Mar 20","Mar 22","Mar 24","Mar 26","Mar 28", "Mar 30","Apr 01"
        )
        private val multiLineSet = listOf(
            linkedMapOf(
                "Feb 1" to 6f,
                "Feb 2" to 15.5f,
                "Feb 4" to 5.7f,
                "Feb 10" to 4.5f,
                "Feb 12" to 4.6f,
                "Feb 14" to 8.5f,
                "Feb 20" to 8.5f,
                "Feb 23" to 11f,
                "Feb 27" to 6f,
                "Mar 01" to 7.5f,
                "Mar 04" to 4f,
                "Mar 06" to 5f
            ),
            linkedMapOf(
                "label1" to 2f,
                "label2" to 2.5f,
                "label3" to 2.7f,
                "label4" to 2.5f,
                "label5" to 5.6f,
                "label6" to 5.5f,
                "label7" to 4.5f,
                "label8" to 5f,
                "label9" to 6f,
                "label10" to 6.5f,
                "label11" to 7f,
                "label12" to 8f
            )
            ,
            linkedMapOf(
                "label1" to 1f,
                "label2" to 1.3f,
                "label3" to 1.7f,
                "label4" to 2.1f,
                "label5" to 2.7f,
                "label6" to 3.5f,
                "label7" to 4.5f,
                "label8" to 6f,
                "label9" to 8.5f,
                "label10" to 15f
            )
            ,
            linkedMapOf(
                "label1" to 4f,
                "label2" to 3.3f,
                "label3" to 4.7f,
                "label4" to 4.1f,
                "label5" to 5.7f,
                "label6" to 6.5f,
                "label7" to 3.5f,
                "label8" to 2f,
                "label9" to 2.5f,
                "label10" to 2f
            ))
        private const val animationDuration = 1000L
    }
}