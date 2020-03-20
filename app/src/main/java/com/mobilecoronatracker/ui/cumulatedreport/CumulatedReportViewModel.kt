package com.mobilecoronatracker.ui.cumulatedreport

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilecoronatracker.data.source.CovidCumulatedDataObserver
import com.mobilecoronatracker.data.source.CovidDataSource
import com.mobilecoronatracker.model.GeneralReportModelable

class CumulatedReportViewModel(private val dataSource: CovidDataSource) : ViewModel(),
    CumulatedReportViewModelable,
    CovidCumulatedDataObserver {
    override val cases = MutableLiveData<String>()
    override val deaths = MutableLiveData<String>()
    override val recovered = MutableLiveData<String>()

    init {
        cases.value = "???"
        deaths.value = "???"
        recovered.value = "???"
        dataSource.addCovidCumulatedDataObserver(this)
    }

    override fun onCumulatedData(data: GeneralReportModelable) {
        cases.postValue(data.cases.toString())
        deaths.postValue(data.deaths.toString())
        recovered.postValue(data.recovered.toString())
    }

    override fun onError() {
        Log.e(CumulatedReportViewModel::class.java.simpleName, "onError")
    }

    override fun onCleared() {
        super.onCleared()
        dataSource.removeCovidCumulatedDataObserver(this)
    }
}
