package com.mobilecoronatracker.ui.cumulatedreport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilecoronatracker.data.source.CovidCumulatedDataObserver
import com.mobilecoronatracker.data.source.CovidDataSource
import com.mobilecoronatracker.data.source.impl.CovidRestDataReader
import com.mobilecoronatracker.model.GeneralReportModelable

class CumulatedReportViewModel : ViewModel(), CumulatedReportViewModelable, CovidCumulatedDataObserver {
    override val cases = MutableLiveData<String>()
    override val deaths = MutableLiveData<String>()
    override val recovered = MutableLiveData<String>()
    private var dataSource: CovidDataSource = CovidRestDataReader()

    init {
        cases.value = "???"
        deaths.value = "???"
        recovered.value = "???"
        dataSource.setRefreshInterval(5)
        dataSource.addCovidCumulatedDataObserver(this)
    }

    override fun onCumulatedData(data: GeneralReportModelable) {
        cases.postValue(data.cases.toString())
        deaths.postValue(data.deaths.toString())
        recovered.postValue(data.recovered.toString())
    }
}