package com.mobilecoronatracker.ui.cumulatedreport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilecoronatracker.data.repository.AccumulatedDataRepo
import com.mobilecoronatracker.data.repository.RepoInitializer
import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.model.GeneralReportTimePointModelable
import com.mobilecoronatracker.utils.asSimpleDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CumulatedReportViewModel(
    private val accumulatedDataRepo: AccumulatedDataRepo,
    private val repoInitializer: RepoInitializer
) : ViewModel(),
    CumulatedReportViewModelable {
    override val cases = MutableLiveData<String>()
    override val active = MutableLiveData<String>()
    override val deaths = MutableLiveData<String>()
    override val recovered = MutableLiveData<String>()
    override var casesCount = 0
    override var activeCount = 0
    override var deathsCount = 0
    override var recoveredCount = 0
    override val todayUpdated = MutableLiveData<Void>()
    override val casesHistory = MutableLiveData<List<Int>>()
    override val deathsHistory = MutableLiveData<List<Int>>()
    override val recoveredHistory = MutableLiveData<List<Int>>()
    override var history = listOf<List<Float>>()
    override var historyLabels = listOf<String>()
    override val historyUpdated = MutableLiveData<Void>()
    override val isRefreshing = MutableLiveData<Boolean>()

    init {
        cases.value = "???"
        deaths.value = "???"
        recovered.value = "???"
        viewModelScope.launch(Dispatchers.IO) {
            if (!accumulatedDataRepo.hasAnyData()) {
                repoInitializer.start()
            }
            if (!accumulatedDataRepo.hasTodayData()) {
                refreshData()
            }

            accumulatedDataRepo.getFullHistory().collect {
                onCumulatedHistory(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            accumulatedDataRepo.getTodayData().collect {
                onCumulatedData(it)
            }
        }
    }

    private fun refreshData() {
        isRefreshing.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            accumulatedDataRepo.refreshAccumulatedData()
        }
    }

    override fun onRefreshRequested() {
        refreshData()
    }

    private fun onCumulatedData(data: GeneralReportModelable) {
        cases.postValue(data.cases.toString())
        active.postValue((data.cases - data.deaths - data.recovered).toString())
        deaths.postValue(data.deaths.toString())
        casesCount = data.cases
        deathsCount = data.deaths
        recoveredCount = data.recovered
        activeCount = casesCount - deathsCount - recoveredCount
        recovered.postValue(data.recovered.toString())
        isRefreshing.postValue(false)
        todayUpdated.postValue(null)
    }

    private fun onCumulatedHistory(data: List<GeneralReportTimePointModelable>) {
        val cases = mutableListOf<Float>()
        val deaths = mutableListOf<Float>()
        val recovered = mutableListOf<Float>()
        val labels = mutableListOf<String>()
        data.forEach {
            cases.add(it.cases.toFloat())
            deaths.add(it.deaths.toFloat())
            recovered.add(it.recovered.toFloat())
            labels.add(it.timestamp.asSimpleDate())
        }
        history = listOf(cases, recovered, deaths)
        historyLabels = labels
        historyUpdated.postValue(null)
    }
}
