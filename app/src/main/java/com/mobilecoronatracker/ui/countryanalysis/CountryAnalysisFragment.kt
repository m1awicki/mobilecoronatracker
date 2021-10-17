package com.mobilecoronatracker.ui.countryanalysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.FragmentCountryAnalysisBinding
import com.mobilecoronatracker.ui.chart.williamchart.view.ImplementsAlphaChart
import org.koin.androidx.viewmodel.ext.android.viewModel

@ImplementsAlphaChart
class CountryAnalysisFragment : Fragment() {
    private val viewModel: CountryAnalysisViewModelable by viewModel<CountryAnalysisViewModel>()
    private val args: CountryAnalysisFragmentArgs by navArgs()

    private var _binding: FragmentCountryAnalysisBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCountryAnalysisBinding.inflate(inflater, container, false)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        setupDonutChart()
        setupHistoryChart()
        setupActiveHistoryChart()
        setupPerMillionDataChart()
        setupDailyIncreaseChart()
        setupDailyIncreaseAsPercentChart()
    }

    private fun setupDonutChart() = with(binding.testedToIdentifiedChart) {
        donutColors = intArrayOf(
            resources.getColor(R.color.recovered, null),
            resources.getColor(R.color.existing, null)
        )
        animation.duration = defaultAnimationDuration
        animate(initialDonutChartValues)
    }

    private fun setupHistoryChart() = with(binding.countryHistoryChart) {
        animation.duration = defaultAnimationDuration
        labelsFormatter = { data -> data.toInt().toString() }
        setLineColors(
            listOf(
                resources.getColor(R.color.existing, null),
                resources.getColor(R.color.recovered, null),
                resources.getColor(R.color.dead, null)
            )
        )
    }

    private fun setupActiveHistoryChart() = with(binding.countryActiveHistoryChart) {
        animation.duration = defaultAnimationDuration
        labelsFormatter = { data -> data.toInt().toString() }
        setLineColors(
            listOf(
                resources.getColor(R.color.existing, null)
            )
        )
    }

    private fun setupDailyIncreaseChart() = with(binding.dailyIncreaseChart) {
        animation.duration = defaultAnimationDuration
        labelsFormatter = { data -> data.toInt().toString() }
        setLineColors(
            listOf(
                resources.getColor(R.color.existing, null)
            )
        )
    }

    private fun setupDailyIncreaseAsPercentChart() =
        with(binding.dailyIncreaseAsPercentOfAllChart) {
            animation.duration = defaultAnimationDuration
            labelsFormatter = { data -> data.toInt().toString() }
            setLineColors(
                listOf(
                    resources.getColor(R.color.existing, null)
                )
            )
        }

    private fun setupPerMillionDataChart() = with(binding.dataPerMillionChart) {
        labelsFormatter = { data -> data.toInt().toString() }
        animation.duration = defaultAnimationDuration
        barsColor = resources.getColor(R.color.existing, null)
        labelsColor = resources.getColor(R.color.gray, null)
    }

    private fun bindObservers() {
        viewModel.testedToIdentifiedData.observe(viewLifecycleOwner, {
            binding.testedToIdentifiedChart.donutTotal = it.reduce { acc, fl -> acc + fl }
            binding.testedToIdentifiedChart.animate(it)
        })
        viewModel.casesPerMillionData.observe(viewLifecycleOwner, {
            binding.dataPerMillionChart.animate(it.labels, it.values)
        })
        viewModel.countryHistoryData.observe(viewLifecycleOwner, {
            binding.countryHistoryChart.animate(it.labels, it.timeLines)
        })
        viewModel.countryActiveCasesData.observe(viewLifecycleOwner, {
            binding.countryActiveHistoryChart.animate(it.labels, listOf(it.values))
        })
        viewModel.dailyIncreaseData.observe(viewLifecycleOwner, {
            binding.dailyIncreaseChart.animate(it.labels, listOf(it.values))
        })
        viewModel.dailyIncreaseAsPercentOfAllData.observe(viewLifecycleOwner, {
            binding.dailyIncreaseAsPercentOfAllChart.animate(it.labels, listOf(it.values))
        })
        viewModel.countryFlagPath.observe(viewLifecycleOwner, {
            binding.countryFlag.load(it)
        })
    }

    companion object {
        const val defaultAnimationDuration = 1500L
        val initialDonutChartValues = listOf(0f)
    }
}