package com.mobilecoronatracker.ui.countrieslist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilecoronatracker.data.repository.CountriesDataRepo
import com.mobilecoronatracker.data.repository.CountriesFollowRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Locale


@ExperimentalCoroutinesApi
class CountriesListViewModel(
    private val countriesFollowRepo: CountriesFollowRepo,
    private val countriesDataRepo: CountriesDataRepo
) : ViewModel(),
    CountriesListViewModelable {
    private var currentList: List<CountriesListViewModelable.CountryReport> = emptyList()
    private var currentFilterText: String = ""
    override val countryReports = MutableLiveData<List<CountriesListViewModelable.CountryReport>>()
    override val isRefreshing = MutableLiveData<Boolean>()
    override val navigationToCountryRequested = MutableLiveData<String>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (!countriesDataRepo.hasTodayCountryData()) {
                refreshData()
            }

            countriesDataRepo.getAllCountriesTodayData()
                .combine(countriesDataRepo.getAllCountriesYesterdayData()) { today, yesterday -> today to yesterday }
                .collect { (todayList, yesterdayList) ->
                    val todayMap = todayList.map { it.iso2 to it }.toMap()
                    val deltas = yesterdayList.mapNotNull { yesterday ->
                        val today = todayMap[yesterday.iso2]
                        today?.let {
                            val casesDelta = it.cases.minus(yesterday.cases)
                            val deathsDelta = it.deaths.minus(yesterday.deaths)
                            val recoveredDelta = it.recovered.minus(yesterday.recovered)

                            CountriesListViewModelable.CountryReport(
                                casesDelta = casesDelta,
                                deathsDelta = deathsDelta,
                                recoveredDelta = recoveredDelta,
                                countryReport = today
                            )
                        }
                    }
                    currentList = deltas
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

    override fun onShareReport(report: CountriesListViewModelable.CountryReport) {
        Log.d(CountriesListViewModel::class.java.simpleName, "onShareReport() not implemented!")
    }

    override fun onCountryAnalysisRequested(countryName: String) {
        navigationToCountryRequested.postValue(countryName)
    }

    private fun refreshData() {
        isRefreshing.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            countriesDataRepo.fetchTodayCountriesData()
        }
    }

    private fun postFilteredList() {
        val filtered = getFilteredList()
        countryReports.postValue(getFollowStatusList(filtered))
        isRefreshing.postValue(false)
    }

    private fun getFilteredList(): List<CountriesListViewModelable.CountryReport> {
        return currentList.filter {
            val countryName = it.countryReport.country.lowercase(Locale.getDefault())
            val countryIso = it.countryReport.iso2.lowercase(Locale.getDefault())
            countryName.contains(currentFilterText) || countryIso.contains(currentFilterText)
        }
    }

    private fun getFollowStatusList(filtered: List<CountriesListViewModelable.CountryReport>):
            List<CountriesListViewModelable.CountryReport> {
        val followedCountries = countriesFollowRepo.getFollowedCountries()
        val followed = filtered.filter {
            followedCountries.contains(it.countryReport.country)
        }.map {
            it.countryReport.followed = true
            it
        }
        val notFollowed = filtered.filterNot {
            followedCountries.contains(it.countryReport.country)
        }.map {
            it.countryReport.followed = false
            it
        }
        return followed + notFollowed
    }
}
