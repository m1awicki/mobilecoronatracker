package com.mobilecoronatracker.ui.cumulatedreport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilecoronatracker.data.repository.AccumulatedDataRepo
import com.mobilecoronatracker.data.repository.RepoInitializer
import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.ui.cumulatedreport.CumulatedReportViewModelable.CurrentStateChartData
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
    override val currentStateChart = MutableLiveData<CurrentStateChartData>()
    override val isRefreshing = MutableLiveData<Boolean>()
    override val navigateToChartsEvent = MutableLiveData<Boolean>()


    init {
        cases.value = "???"
        deaths.value = "???"
        recovered.value = "???"
        active.value = "???"
        viewModelScope.launch(Dispatchers.IO) {
            if (!accumulatedDataRepo.hasAnyData()) {
                repoInitializer.start()
            }
            if (!accumulatedDataRepo.hasTodayData()) {
                refreshData()
            }

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

    override fun onChartsButtonClicked() {
        navigateToChartsEvent.value = true
    }

    private fun onCumulatedData(data: GeneralReportModelable) {
        cases.postValue(data.cases.toString())
        active.postValue((data.cases - data.deaths - data.recovered).toString())
        deaths.postValue(data.deaths.toString())
        recovered.postValue(data.recovered.toString())
        val active = data.cases - data.deaths - data.recovered
        currentStateChart.postValue(
            CurrentStateChartData(
                active,
                data.recovered,
                data.deaths
            )
        )
        isRefreshing.postValue(false)
    }
}
