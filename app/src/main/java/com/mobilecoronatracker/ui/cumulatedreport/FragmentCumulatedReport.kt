package com.mobilecoronatracker.ui.cumulatedreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.FragmentCumulatedReportBinding
import com.mobilecoronatracker.ui.chart.williamchart.view.ImplementsAlphaChart
import kotlinx.android.synthetic.main.fragment_cumulated_report.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@ImplementsAlphaChart
class FragmentCumulatedReport : Fragment() {
    private val viewModel: CumulatedReportViewModelable by viewModel<CumulatedReportViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val binding = DataBindingUtil.inflate<FragmentCumulatedReportBinding>(
            inflater, R.layout.fragment_cumulated_report, container, false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        bindObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reports_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                viewModel.onRefreshRequested()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupDonutChart() {
        cumulated_data_chart.donutColors = intArrayOf(
            resources.getColor(R.color.existing, null),
            resources.getColor(R.color.recovered, null),
            resources.getColor(R.color.dead, null)
        )
        cumulated_data_chart.animation.duration = chartAnimationDuration
        cumulated_data_chart.animate(initialDonutChartData)
    }


    private fun setupViews() {
        cumulated_report_swipe_refresh.setOnRefreshListener {
            viewModel.onRefreshRequested()
        }
        setupDonutChart()
    }

    private fun bindObservers() {
        viewModel.navigateToChartsEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.navigateToChartsEvent.value = false
                findNavController().navigate(FragmentCumulatedReportDirections.actionNavigationCumulatedToAccumulatedChartsFragment())
            }
        })

        viewModel.currentStateChart.observe(viewLifecycleOwner, Observer {
            val donutData = listOf(
                it.deaths.toFloat(),
                it.recovered.toFloat(),
                it.active.toFloat()
            )
            cumulated_data_chart.donutTotal = it.active.toFloat()
            cumulated_data_chart.animate(donutData)
        })
    }

    companion object {
        const val chartAnimationDuration = 1500L
        val initialDonutChartData = listOf(0f, 0f, 0f)
    }
}
