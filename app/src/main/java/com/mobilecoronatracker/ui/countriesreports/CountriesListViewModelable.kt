package com.mobilecoronatracker.ui.countriesreports

import androidx.lifecycle.LiveData
import com.mobilecoronatracker.model.CountryReportModelable

interface CountriesListViewModelable {
    fun onFilterTextChanged(text: String)

    val countryReports: LiveData<List<CountryReportModelable>>
}