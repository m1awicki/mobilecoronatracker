package com.mobilecoronatracker.ui.countriesreports

import androidx.lifecycle.LiveData
import com.mobilecoronatracker.model.CountryReportModelable

interface CountriesListViewModelable {
    fun onFilterTextChanged(text: String)
    fun onRefreshRequested()

    val countryReports: LiveData<List<CountryReportModelable>>
    val isRefreshing: LiveData<Boolean>
    fun onCountryFollowed(countryName: String)
    fun onCountryUnfollowed(countryName: String)
}
