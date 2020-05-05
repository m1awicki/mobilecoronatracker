package com.mobilecoronatracker.ui.countrieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobilecoronatracker.model.CountryReportModelable

interface CountriesListViewModelable :
    CountryFollowListener,
    ShareCountryReportListener,
    ShowCountryAnalysisListener
{
    val navigationToCountryRequested: MutableLiveData<String>

    fun onFilterTextChanged(text: String)
    fun onRefreshRequested()

    val countryReports: LiveData<List<CountryReport>>
    val isRefreshing: LiveData<Boolean>

    data class CountryReport(val casesDelta: Int, val deathsDelta: Int, val recoveredDelta: Int,
                             val countryReport: CountryReportModelable)
}