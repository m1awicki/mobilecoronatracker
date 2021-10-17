package com.mobilecoronatracker.ui.accumulatedcharts

import androidx.lifecycle.LiveData

interface AccumulatedChartsViewModelable {
    val historyChartUpdate: LiveData<HistoryChartData>
    val activeCasesChartUpdate: LiveData<HistoryChartData>
    val affectedCountries: LiveData<Int>
    val globalDataWithToday: LiveData<GlobalDataWithToday>


    data class GlobalDataWithToday(
        val cases: Int, val casesToday: Int, val deaths: Int, val deathsToday: Int, val active: Int,
        val recovered: Int, val recoveredToday: Int, val critical: Int, val tested: Long
    )
    data class HistoryChartData(val timeLines: List<List<Float>>, val labels: List<String>)
}