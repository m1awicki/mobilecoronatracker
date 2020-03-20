package com.mobilecoronatracker.ui.countriesreports

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilecoronatracker.data.persistence.CountriesFollowRepo
import com.mobilecoronatracker.data.source.CovidCountriesDataObserver
import com.mobilecoronatracker.data.source.CovidDataSource
import com.mobilecoronatracker.model.CountryReportModelable
import java.util.Locale

class CountriesListViewModel(
    private val countriesFollowRepo: CountriesFollowRepo,
    private val dataSource: CovidDataSource
) : ViewModel(), CountriesListViewModelable, CovidCountriesDataObserver {
    private var currentList: List<CountryReportModelable> = emptyList()
    private var currentFilterText: String = ""
    override val countryReports = MutableLiveData<List<CountryReportModelable>>()
    override val isRefreshing = MutableLiveData<Boolean>()

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

    override fun onFollowRequested(countryName: String) {
        countriesFollowRepo.addFollowedCountry(countryName)
        postFilteredList()
    }

    override fun onUnfollowRequested(countryName: String) {
        countriesFollowRepo.removeFollowedCountry(countryName)
        postFilteredList()
    }

    override fun onCleared() {
        super.onCleared()
        dataSource.removeCovidCountriesDataObserver(this)
    }

    private fun postFilteredList() {
        val filtered = getFilteredList()
        countryReports.postValue(getFollowStatusList(filtered))
        isRefreshing.postValue(false)
    }

    private fun getFilteredList(): List<CountryReportModelable> {
        return currentList.filter {
            it.country
                .toLowerCase(Locale.getDefault())
                .contains(
                    currentFilterText.toLowerCase(Locale.getDefault())
                )
        }
    }

    private fun getFollowStatusList(filtered: List<CountryReportModelable>): List<CountryReportModelable> {
        val followedCountries = countriesFollowRepo.getFollowedCountries()
        val followed = filtered.filter {
            followedCountries.contains(it.country)
        }.map {
            it.followed = true
            it
        }
        val notFollowed = filtered.filterNot {
            followedCountries.contains(it.country)
        }.map {
            it.followed = false;
            it
        }
        return followed + notFollowed
    }
}
