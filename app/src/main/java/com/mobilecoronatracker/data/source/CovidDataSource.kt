package com.mobilecoronatracker.data.source

interface CovidDataSource {
    fun setRefreshInterval(seconds: Long)
    fun addCovidCountriesDataObserver(observerCountries: CovidCountriesDataObserver)
    fun removeCovidCountriesDataObserver(observerCountries: CovidCountriesDataObserver)
    fun addCovidCumulatedDataObserver(observerCountries: CovidCumulatedDataObserver)
    fun removeCovidCumulatedDataObserver(observerCountries: CovidCumulatedDataObserver)
    fun requestData()
}