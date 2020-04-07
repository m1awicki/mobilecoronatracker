package com.mobilecoronatracker.ui.cumulatedreport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface CumulatedReportViewModelable {
    val cases: LiveData<String>
    val active: LiveData<String>
    val deaths: LiveData<String>
    val recovered: LiveData<String>
    var casesCount: Int
    var activeCount: Int
    var deathsCount: Int
    var recoveredCount: Int
    val todayUpdated: LiveData<Void>
    val casesHistory: LiveData<List<Int>>
    val deathsHistory: LiveData<List<Int>>
    val recoveredHistory: LiveData<List<Int>>
    var history: List<List<Float>>
    var historyLabels: List<String>
    val historyUpdated: LiveData<Void>
    val isRefreshing: MutableLiveData<Boolean>
    fun onRefreshRequested()
}
