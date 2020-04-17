package com.mobilecoronatracker.ui.accumulatedcharts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilecoronatracker.data.repository.AccumulatedDataRepo
import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.model.GeneralReportTimePointModelable
import com.mobilecoronatracker.ui.accumulatedcharts.AccumulatedChartsViewModelable.HistoryChartData
import com.mobilecoronatracker.utils.asSimpleDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AccumulatedChartsViewModel(
    private val accumulatedDataRepo: AccumulatedDataRepo
) : ViewModel(), AccumulatedChartsViewModelable {
    override val historyChartUpdate = MutableLiveData<HistoryChartData>()
    override val activeCasesChartUpdate = MutableLiveData<HistoryChartData>()
    override val affectedCountries = MutableLiveData<Int>()
    override val globalDataWithToday = MutableLiveData<AccumulatedChartsViewModelable.GlobalDataWithToday>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            accumulatedDataRepo.getFullHistory().collect {
                onAccumulatedHistory(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            accumulatedDataRepo.getDataForLastDays(2).collect {
                onRecentDays(it)
            }
        }
    }

    private fun onRecentDays(data: List<GeneralReportModelable>) {
        val today = data.last()
        val yesterday = data.first()
        affectedCountries.postValue(today.affectedCountries)
        globalDataWithToday.postValue(
            AccumulatedChartsViewModelable.GlobalDataWithToday(
                today.cases, today.todayCases, today.deaths, today.todayDeaths,
                today.cases - today.deaths - today.recovered, today.recovered,
                today.recovered - yesterday.recovered, today.critical, today.tests
            )
        )
    }

    private fun onAccumulatedHistory(data: List<GeneralReportTimePointModelable>) {
        val cases = mutableListOf<Float>()
        val deaths = mutableListOf<Float>()
        val recovered = mutableListOf<Float>()
        val active = mutableListOf<Float>()
        val labels = mutableListOf<String>()
        data.forEach { point ->
            cases.add(point.cases.toFloat())
            deaths.add(point.deaths.toFloat())
            recovered.add(point.recovered.toFloat())
            active.add((point.cases - point.deaths - point.recovered).toFloat())
            labels.add(point.timestamp.asSimpleDate())
        }
        historyChartUpdate.postValue(
            HistoryChartData(
                listOf(cases, recovered, deaths),
                labels
            )
        )
        activeCasesChartUpdate.postValue(
            HistoryChartData(
                listOf(active),
                labels
            )
        )
    }
}
