package com.mobilecoronatracker.ui.cumulatedreport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilecoronatracker.data.repository.AccumulatedDataRepo
import com.mobilecoronatracker.model.GeneralReportModelable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CumulatedReportViewModel(private val accumulatedDataRepo: AccumulatedDataRepo) : ViewModel(),
    CumulatedReportViewModelable {
    override val cases = MutableLiveData<String>()
    override val deaths = MutableLiveData<String>()
    override val recovered = MutableLiveData<String>()
    override val isRefreshing = MutableLiveData<Boolean>()

    init {
        cases.value = "???"
        deaths.value = "???"
        recovered.value = "???"
        viewModelScope.launch(Dispatchers.IO) {
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

    private fun onCumulatedData(data: GeneralReportModelable) {
        cases.postValue(data.cases.toString())
        deaths.postValue(data.deaths.toString())
        recovered.postValue(data.recovered.toString())
        isRefreshing.postValue(false)
    }

}
