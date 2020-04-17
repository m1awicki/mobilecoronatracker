package com.mobilecoronatracker.ui.countryanalysis

import androidx.lifecycle.LiveData

interface CountryAnalysisViewModelable {
    val countryHistoryData: LiveData<HistoryChartData>
    val countryActiveCasesData: LiveData<SingleLineChartData>
    val dailyIncreaseData: LiveData<SingleLineChartData>
    val dailyIncreaseAsPercentOfAllData: LiveData<SingleLineChartData>
    val casesPerMillionData: LiveData<CasesPerMillionData>
    val testedToIdentifiedData: LiveData<List<Float>>
    val tested: LiveData<Int>
    val infected: LiveData<Int>
    val countryFlagPath: LiveData<String>
    val countryName: LiveData<String>

    fun setCountry(name: String)

    data class CasesPerMillionData(val values: List<Float>, val labels: List<String>)
    data class HistoryChartData(val timeLines: List<List<Float>>, val labels: List<String>)
    data class SingleLineChartData(val values: List<Float>, val labels: List<String>)
}