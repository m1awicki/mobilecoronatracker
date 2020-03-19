package com.mobilecoronatracker.ui.countriesreports

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilecoronatracker.data.persistence.CountriesReportsSettings
import com.mobilecoronatracker.data.source.CovidCountriesDataObserver
import com.mobilecoronatracker.data.source.CovidDataSource
import com.mobilecoronatracker.data.source.impl.CovidRestDataReader
import com.mobilecoronatracker.model.CountryReportModelable
import java.util.Locale

class CountriesListViewModel(private val countriesReportsSettings: CountriesReportsSettings) :
    ViewModel(), CountriesListViewModelable, CovidCountriesDataObserver {
    private var currentList: List<CountryReportModelable> = emptyList()
    private var currentFilterText: String = ""
    override val countryReports = MutableLiveData<List<CountryReportModelable>>()
    override val isRefreshing = MutableLiveData<Boolean>()
    private var dataSource: CovidDataSource = CovidRestDataReader()

    init {
        isRefreshing.postValue(true)
        dataSource.addCovidCountriesDataObserver(this)
    }

    override fun onCountriesData(data: List<CountryReportModelable>) {
        currentList = data
        postFilteredList()
    }

    override fun onError() {
        Log.e(CountriesListViewModel::class.java.simpleName, "onError")
        isRefreshing.postValue(false)
    }

    override fun onFilterTextChanged(text: String) {
        currentFilterText = text
        postFilteredList()
    }

    override fun onRefreshRequested() {
        isRefreshing.postValue(true)
        dataSource.requestData()
    }

    fun onCountryFollowed(countryName: String) {
        countriesReportsSettings.addFollowedCountry(countryName)
    }

    fun onCountryUnfollowed(countryName: String) {
        countriesReportsSettings.removeFollowedCountry(countryName)
    }

    private fun postFilteredList() {
        val filtered = currentList.filter {
            it.country
                .toLowerCase(Locale.getDefault())
                .contains(
                    currentFilterText.toLowerCase(Locale.getDefault())
                )
        }
        val followedCountries = countriesReportsSettings.getFollowedCountries()

        countryReports.postValue(
            filtered.filter {
                followedCountries.contains(it.country)
            }.map { it} + filtered.filter {
                !followedCountries.contains(
                    it.country
                )
            })
        isRefreshing.postValue(false)
    }
}