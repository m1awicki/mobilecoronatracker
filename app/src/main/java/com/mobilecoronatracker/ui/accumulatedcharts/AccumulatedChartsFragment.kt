package com.mobilecoronatracker.ui.accumulatedcharts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.AccumulatedChartsFragmentBinding
import com.mobilecoronatracker.ui.chart.williamchart.view.ImplementsAlphaChart
import kotlinx.android.synthetic.main.accumulated_charts_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@ImplementsAlphaChart
class AccumulatedChartsFragment : Fragment() {

    private val viewModel: AccumulatedChartsViewModelable by viewModel<AccumulatedChartsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<AccumulatedChartsFragmentBinding>(
            inflater, R.layout.accumulated_charts_fragment, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
    }

    private fun setupDonutCharts() {
        critical_to_active.donutColors = intArrayOf(
            resources.getColor(R.color.existing, null),
            resources.getColor(R.color.critical, null)
        )
        critical_to_active.animation.duration = chartAnimationDuration
        critical_to_active.animate(initialDonutChartsValues)

        tested_to_infected.donutColors = intArrayOf(
            resources.getColor(R.color.recovered, null),
            resources.getColor(R.color.existing, null)
        )
        tested_to_infected.animation.duration = chartAnimationDuration
        tested_to_infected.animate(initialDonutChartsValues)
    }

    private fun setupLinearCharts() {
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

    private fun setupViews() {
        setupLinearCharts()
        setupDonutCharts()
    }

    private fun setupObservers() {
        viewModel.historyChartUpdate.observe(viewLifecycleOwner, Observer {
            accumulated_history_chart.animate(it.labels, it.timeLines)
        })
        viewModel.activeCasesChartUpdate.observe(viewLifecycleOwner, Observer {
            active_cases_history_chart.animate(it.labels, it.timeLines)
        })
        viewModel.globalDataWithToday.observe(viewLifecycleOwner, Observer {
            val criticalToActive = listOf(it.critical.toFloat(), it.active.toFloat())
            critical_to_active.donutTotal = criticalToActive.reduce { acc, fl -> acc + fl }
            critical_to_active.animate(criticalToActive)

            val testedToInfected = listOf(it.cases.toFloat(), it.tested.toFloat())
            tested_to_infected.donutTotal = testedToInfected.reduce { acc, fl -> acc + fl }
            tested_to_infected.animate(testedToInfected)
        })
    }

    companion object {
        const val chartAnimationDuration = 1500L
        val initialDonutChartsValues = listOf(0f)
    }

}
