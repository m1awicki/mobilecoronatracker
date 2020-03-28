package com.mobilecoronatracker.data.repository

import com.mobilecoronatracker.model.CountryReportModelable
import com.mobilecoronatracker.model.GeneralReportModelable
import kotlinx.coroutines.flow.Flow

interface CountriesDataRepo {
    fun getAllCountriesTodayData(): Flow<List<CountryReportModelable>>
    suspend fun refreshCountriesData()
    suspend fun hasTodayCountryData(): Boolean
}
