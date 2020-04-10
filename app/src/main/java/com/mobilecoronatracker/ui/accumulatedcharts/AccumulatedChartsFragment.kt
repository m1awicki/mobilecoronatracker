package com.mobilecoronatracker.ui.accumulatedcharts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mobilecoronatracker.R
import kotlinx.android.synthetic.main.accumulated_charts_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccumulatedChartsFragment : Fragment() {

    private val viewModel: AccumulatedChartsViewModelable by viewModel<AccumulatedChartsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.accumulated_charts_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLinearChart()

        viewModel.historyChartUpdate.observe(viewLifecycleOwner, Observer {
            accumulated_history_chart.animate(it.labels, it.timeLines)
        })
        viewModel.activeCasesChartUpdate.observe(viewLifecycleOwner, Observer {
            active_cases_history_chart.animate(it.labels, it.timeLines)
        })
    }

    private fun setupLinearChart() {
        accumulated_history_chart.animation.duration = chartAnimationDuration
        accumulated_history_chart.labelsFormatter = { data -> data.toInt().toString() }
        accumulated_history_chart.setLineColors(
            listOf(
                resources.getColor(R.color.existing, null),
                resources.getColor(R.color.recovered, null),
                resources.getColor(R.color.dead, null)
            )
        )

        active_cases_history_chart.animation.duration = chartAnimationDuration
        active_cases_history_chart.labelsFormatter = { data -> data.toInt().toString() }
        active_cases_history_chart.setLineColors(
            listOf(
                resources.getColor(R.color.existing, null)
            )
        )
    }


    companion object {
        const val chartAnimationDuration = 1500L
    }

}
