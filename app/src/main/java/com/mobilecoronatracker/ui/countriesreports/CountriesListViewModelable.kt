package com.mobilecoronatracker.ui.countriesreports

import androidx.lifecycle.LiveData
import com.mobilecoronatracker.model.CountryReportModelable

interface CountriesListViewModelable {
    val countryReports: LiveData<List<CountryReportModelable>>
}