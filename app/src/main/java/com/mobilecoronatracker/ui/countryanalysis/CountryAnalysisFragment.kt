package com.mobilecoronatracker.ui.countryanalysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.FragmentCountryAnalysisBinding
import com.mobilecoronatracker.ui.chart.williamchart.view.ImplementsAlphaChart
import kotlinx.android.synthetic.main.fragment_country_analysis.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@ImplementsAlphaChart
class CountryAnalysisFragment : Fragment() {
    private val viewModel: CountryAnalysisViewModelable by viewModel<CountryAnalysisViewModel>()
    val args: CountryAnalysisFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCountryAnalysisBinding>(
            inflater, R.layout.fragment_country_analysis, container, false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setCountry(args.countryName)
        setupViews()
        bindObservers()
    }

    private fun setupViews() {
        setupDonutChart()
        setupHistoryChart()
        setupActiveHistoryChart()
        setupPerMillionDataChart()
        setupDailyIncreaseChart()
        setupDailyIncreaseAsPercentChart()
    }

    private fun setupDonutChart() {
        tested_to_identified_chart.donutColors = intArrayOf(
            resources.getColor(R.color.recovered, null),
            resources.getColor(R.color.existing, null)
        )
        tested_to_identified_chart.animation.duration = defaultAnimationDuration
        tested_to_identified_chart.animate(initialDonutChartValues)
    }
    private fun setupHistoryChart() {
        country_history_chart.animation.duration = defaultAnimationDuration
        country_history_chart.labelsFormatter = { data -> data.toInt().toString() }
        country_history_chart.setLineColors(
            listOf(
                resources.getColor(R.color.existing, null),
                resources.getColor(R.color.recovered, null),
                resources.getColor(R.color.dead, null)
            )
        )
    }
    private fun setupActiveHistoryChart() {
        country_active_history_chart.animation.duration = defaultAnimationDuration
        country_active_history_chart.labelsFormatter = { data -> data.toInt().toString() }
        country_active_history_chart.setLineColors(
            listOf(
                resources.getColor(R.color.existing, null)
            )
        )
    }
    private fun setupDailyIncreaseChart() {
        daily_increase_chart.animation.duration = defaultAnimationDuration
        daily_increase_chart.labelsFormatter = { data -> data.toInt().toString() }
        daily_increase_chart.setLineColors(
            listOf(
                resources.getColor(R.color.existing, null)
            )
        )
    }
    private fun setupDailyIncreaseAsPercentChart() {
        daily_increase_as_percent_of_all_chart.animation.duration = defaultAnimationDuration
        daily_increase_as_percent_of_all_chart.labelsFormatter = { data -> data.toInt().toString() }
        daily_increase_as_percent_of_all_chart.setLineColors(
            listOf(
                resources.getColor(R.color.existing, null)
            )
        )
    }
    private fun setupPerMillionDataChart() {
        data_per_million_chart.labelsFormatter = { data -> data.toInt().toString() }
        data_per_million_chart.animation.duration = defaultAnimationDuration
        data_per_million_chart.barsColor = resources.getColor(R.color.existing, null)
        data_per_million_chart.labelsColor = resources.getColor(R.color.gray, null)
    }

    private fun bindObservers() {
        viewModel.testedToIdentifiedData.observe(viewLifecycleOwner, Observer {
            tested_to_identified_chart.donutTotal = it.reduce { acc, fl -> acc + fl }
            tested_to_identified_chart.animate(it)
        })
        viewModel.casesPerMillionData.observe(viewLifecycleOwner, Observer {
            data_per_million_chart.animate(it.labels, it.values)
        })
        viewModel.countryHistoryData.observe(viewLifecycleOwner, Observer {
            country_history_chart.animate(it.labels, it.timeLines)
        })
        viewModel.countryActiveCasesData.observe(viewLifecycleOwner, Observer {
            country_active_history_chart.animate(it.labels, listOf(it.values))
        })
        viewModel.dailyIncreaseData.observe(viewLifecycleOwner, Observer {
            daily_increase_chart.animate(it.labels, listOf(it.values))
        })
        viewModel.dailyIncreaseAsPercentOfAllData.observe(viewLifecycleOwner, Observer {
            daily_increase_as_percent_of_all_chart.animate(it.labels, listOf(it.values))
        })
        viewModel.countryFlagPath.observe(viewLifecycleOwner, Observer {
            country_flag.load(it)
        })
    }

    companion object {
        const val defaultAnimationDuration = 1500L
        val initialDonutChartValues = listOf(0f)
    }
}