package com.mobilecoronatracker.ui.cumulatedreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.FragmentCumulatedReportBinding
import com.mobilecoronatracker.ui.chart.williamchart.view.ImplementsAlphaChart
import org.koin.androidx.viewmodel.ext.android.viewModel

@ImplementsAlphaChart
class CumulatedReportFragment : Fragment() {
    private val viewModel: CumulatedReportViewModelable by viewModel<CumulatedReportViewModel>()
    private var _binding: FragmentCumulatedReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        _binding = FragmentCumulatedReportBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        bindObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun setupDonutChart() = with(binding.cumulatedDataChart) {
        donutColors = intArrayOf(
            resources.getColor(R.color.existing, null),
            resources.getColor(R.color.recovered, null),
            resources.getColor(R.color.dead, null)
        )
        animation.duration = chartAnimationDuration
        animate(initialDonutChartData)
    }


    private fun setupViews() {
        binding.cumulatedReportSwipeRefresh.setOnRefreshListener {
            viewModel.onRefreshRequested()
        }
        setupDonutChart()
    }

    private fun bindObservers() {
        viewModel.navigateToChartsEvent.observe(viewLifecycleOwner, {
            if (it) {
                viewModel.navigateToChartsEvent.value = false
                findNavController().navigate(CumulatedReportFragmentDirections.actionNavigationCumulatedToAccumulatedChartsFragment())
            }
        })

        viewModel.currentStateChart.observe(viewLifecycleOwner, {
            val donutData = listOf(
                it.deaths.toFloat(),
                it.recovered.toFloat(),
                it.active.toFloat()
            )
            binding.cumulatedDataChart.donutTotal = it.active.toFloat()
            binding.cumulatedDataChart.animate(donutData)
        })
    }

    companion object {
        const val chartAnimationDuration = 1500L
        val initialDonutChartData = listOf(0f, 0f, 0f)
    }
}
