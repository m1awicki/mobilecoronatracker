package com.mobilecoronatracker.ui.countriesreports

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilecoronatracker.data.repository.CountriesDataRepo
import com.mobilecoronatracker.data.repository.CountriesFollowRepo
import com.mobilecoronatracker.model.CountryReportModelable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale

class CountriesListViewModel(
    private val countriesFollowRepo: CountriesFollowRepo,
    private val countriesDataRepo: CountriesDataRepo
) : ViewModel(), CountriesListViewModelable {
    private var currentList: List<CountryReportModelable> = emptyList()
    private var currentFilterText: String = ""
    override val countryReports = MutableLiveData<List<CountryReportModelable>>()
    override val isRefreshing = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (countriesDataRepo.hasNoTodayCountryData()) {
                refreshData()
            }

            countriesDataRepo.getAllCountriesTodayData().collect {
                currentList = it
                postFilteredList()
            }
        }
    }

    override fun onFilterTextChanged(text: String) {
        currentFilterText = text
        postFilteredList()
    }

    override fun onRefreshRequested() {
        refreshData()
    }

    override fun onCountryFollowed(countryName: String) {
        countriesFollowRepo.addFollowedCountry(countryName)
        postFilteredList()
    }

    override fun onCountryUnfollowed(countryName: String) {
        countriesFollowRepo.removeFollowedCountry(countryName)
        postFilteredList()
    }

    private fun refreshData() {
        isRefreshing.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            countriesDataRepo.refreshCountriesData()
        }
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
