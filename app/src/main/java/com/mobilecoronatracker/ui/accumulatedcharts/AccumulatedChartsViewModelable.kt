package com.mobilecoronatracker.ui.accumulatedcharts

import androidx.lifecycle.MutableLiveData
import com.mobilecoronatracker.ui.cumulatedreport.CumulatedReportViewModelable

interface AccumulatedChartsViewModelable {
    val historyChartUpdate: MutableLiveData<HistoryChartData>

    data class HistoryChartData(val timeLines: List<List<Float>>, val labels: List<String>)

    val activeCasesChartUpdate: MutableLiveData<HistoryChartData>
}