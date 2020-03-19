package com.mobilecoronatracker.data.source

import com.mobilecoronatracker.model.CountryReportModelable

interface CovidCountriesDataObserver : DataErrorObserver {
    fun onCountriesData(data: List<CountryReportModelable>)
}