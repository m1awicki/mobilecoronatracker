package com.mobilecoronatracker.ui.countriesreports

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mobilecoronatracker.model.CountryReportModelable

interface  CountriesListViewModelable {
    fun onFilterTextChanged(text: String)
    fun onRefreshRequested()
    fun onFollowRequested(countryName: String)
    fun onUnfollowRequested(countryName: String)

    val countryReports: LiveData<List<CountryReportModelable>>
    val isRefreshing: LiveData<Boolean>
}
