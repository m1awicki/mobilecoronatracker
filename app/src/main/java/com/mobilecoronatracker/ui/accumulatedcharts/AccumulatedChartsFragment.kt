package com.mobilecoronatracker.ui.accumulatedcharts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.AccumulatedChartsFragmentBinding
import com.mobilecoronatracker.ui.chart.williamchart.view.ImplementsAlphaChart
import org.koin.androidx.viewmodel.ext.android.viewModel

@ImplementsAlphaChart
class AccumulatedChartsFragment : Fragment() {
    private val viewModel: AccumulatedChartsViewModelable by viewModel<AccumulatedChartsViewModel>()
    private var _binding: AccumulatedChartsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AccumulatedChartsFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDonutCharts() {
        with(binding.criticalToActive) {
            donutColors = intArrayOf(
                resources.getColor(R.color.existing, null),
                resources.getColor(R.color.critical, null)
            )
            animation.duration = chartAnimationDuration
            animate(initialDonutChartsValues)
        }

        with(binding.testedToInfected) {
            donutColors = intArrayOf(
                resources.getColor(R.color.recovered, null),
                resources.getColor(R.color.existing, null)
            )
            animation.duration = chartAnimationDuration
            animate(initialDonutChartsValues)
        }
    }

    private fun setupLinearCharts() {
        with(binding.accumulatedHistoryChart) {
            animation.duration = chartAnimationDuration
            labelsFormatter = { data -> data.toInt().toString() }
            setLineColors(
                listOf(
                    resources.getColor(R.color.existing, null),
                    resources.getColor(R.color.recovered, null),
                    resources.getColor(R.color.dead, null)
                )
            )

            with(binding.activeCasesHistoryChart) {
                animation.duration = chartAnimationDuration
                labelsFormatter = { data -> data.toInt().toString() }
                setLineColors(
                    listOf(
                        resources.getColor(R.color.existing, null)
                    )
                )
            }
        }
    }

    private fun setupViews() {
        setupLinearCharts()
        setupDonutCharts()
    }

    private fun setupObservers() {
        viewModel.historyChartUpdate.observe(viewLifecycleOwner, {
            binding.accumulatedHistoryChart.animate(it.labels, it.timeLines)
        })
        viewModel.activeCasesChartUpdate.observe(viewLifecycleOwner, {
            binding.activeCasesHistoryChart.animate(it.labels, it.timeLines)
        })
        viewModel.globalDataWithToday.observe(viewLifecycleOwner, {
            val criticalToActive = listOf(it.critical.toFloat(), it.active.toFloat())
            binding.criticalToActive.donutTotal = criticalToActive.reduce { acc, fl -> acc + fl }
            binding.criticalToActive.animate(criticalToActive)

            val testedToInfected = listOf(it.cases.toFloat(), it.tested.toFloat())
            binding.testedToInfected.donutTotal = testedToInfected.reduce { acc, fl -> acc + fl }
            binding.testedToInfected.animate(testedToInfected)
        })
    }

    companion object {
        const val chartAnimationDuration = 1500L
        val initialDonutChartsValues = listOf(0f)
    }
}
