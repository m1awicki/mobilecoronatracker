package com.mobilecoronatracker.ui.cumulatedreport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface CumulatedReportViewModelable {
    val cases: LiveData<String>
    val deaths: LiveData<String>
    val recovered: LiveData<String>
    val isRefreshing: MutableLiveData<Boolean>
    fun onRefreshRequested()
}
