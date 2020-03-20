package com.mobilecoronatracker.ui.countriesreports

import androidx.lifecycle.LiveData
import com.mobilecoronatracker.model.CountryReportModelable

interface CountriesListViewModelable : CountryFollowListener {
    fun onFilterTextChanged(text: String)
    fun onRefreshRequested()

    val countryReports: LiveData<List<CountryReportModelable>>
    val isRefreshing: LiveData<Boolean>
}
