package com.mobilecoronatracker.data.source

import com.mobilecoronatracker.model.CountryReportModelable

interface CovidCountriesDataObserver : DataObserver {
    fun onCountriesData(data: List<CountryReportModelable>)
}