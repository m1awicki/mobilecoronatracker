package com.mobilecoronatracker.data.source

import com.mobilecoronatracker.model.CountryReportModelable

interface CovidCountriesDataObserver {
    fun onCountriesData(data: List<CountryReportModelable>)
}