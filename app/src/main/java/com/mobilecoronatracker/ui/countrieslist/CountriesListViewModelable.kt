package com.mobilecoronatracker.ui.countrieslist

import androidx.lifecycle.LiveData
import com.mobilecoronatracker.model.CountryReportModelable

interface CountriesListViewModelable :
    CountryFollowListener,
    ShareCountryReportListener,
    ShowCountryAnalysisListener
{
    fun onFilterTextChanged(text: String)
    fun onRefreshRequested()

    val countryReports: LiveData<List<CountryReport>>
    val isRefreshing: LiveData<Boolean>
    val toggleCollapse: LiveData<Void>

    data class CountryReport(val casesDelta: Int, val deathsDelta: Int, val recoveredDelta: Int,
                             val countryReport: CountryReportModelable)
}