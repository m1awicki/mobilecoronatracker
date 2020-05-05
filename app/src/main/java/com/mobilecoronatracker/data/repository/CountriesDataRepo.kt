package com.mobilecoronatracker.data.repository

import com.mobilecoronatracker.model.CountryReportModelable
import com.mobilecoronatracker.model.CountryReportTimePointModelable
import kotlinx.coroutines.flow.Flow

interface CountriesDataRepo {
    fun getAllCountriesTodayData(): Flow<List<CountryReportModelable>>
    fun getAllCountriesYesterdayData(): Flow<List<CountryReportModelable>>
    fun getCountryHistory(countryName: String): Flow<List<CountryReportTimePointModelable>>
    suspend fun fetchTodayCountriesData(): RepoResult<Unit>
    suspend fun hasTodayCountryData(): Boolean
    suspend fun fetchHistoricalCountriesData()
    suspend fun fetchYesterdayCountriesData(): RepoResult<Unit>
}
