package com.mobilecoronatracker.ui.cumulatedreport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface CumulatedReportViewModelable {
    val cases: LiveData<String>
    val active: LiveData<String>
    val deaths: LiveData<String>
    val recovered: LiveData<String>
    val currentStateChart: LiveData<CurrentStateChartData>
    val isRefreshing: MutableLiveData<Boolean>
    fun onRefreshRequested()
    fun onShowChartClicked()

    data class CurrentStateChartData(val active: Int, val recovered: Int, val deaths: Int)

    val showChartEvent: MutableLiveData<Boolean>
}
