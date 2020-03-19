package com.mobilecoronatracker.data.source

interface CovidDataSource {
    fun setRefreshInterval(seconds: Long)
    fun addCovidCountriesDataObserver(observer: CovidCountriesDataObserver)
    fun removeCovidCountriesDataObserver(observer: CovidCountriesDataObserver)
    fun addCovidCumulatedDataObserver(observer: CovidCumulatedDataObserver)
    fun removeCovidCumulatedDataObserver(observer: CovidCumulatedDataObserver)
    fun requestData()
}