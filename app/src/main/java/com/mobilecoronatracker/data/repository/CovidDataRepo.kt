package com.mobilecoronatracker.data.repository

import com.mobilecoronatracker.data.networking.NetworkResult
import com.mobilecoronatracker.model.CountryReportModelable
import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.model.pojo.CovidAccumulatedHistory
import com.mobilecoronatracker.model.pojo.CovidCountryHistory
import com.mobilecoronatracker.model.pojo.CovidCumulatedData
import com.mobilecoronatracker.ui.cumulatedreport.CumulatedReportViewModelable

interface CovidDataRepo {
    suspend fun getCountriesData(): RepoResult<List<CountryReportModelable>>
    suspend fun getYesterdayCountriesData(): RepoResult<List<CountryReportModelable>>
    suspend fun getCumulatedData(): RepoResult<GeneralReportModelable>
    suspend fun getYesterdayCumulatedData(): RepoResult<GeneralReportModelable>
    suspend fun getCountryHistoricalData(): List<CovidCountryHistory>
    suspend fun getAccumulatedHistoricalData(): CovidAccumulatedHistory
}
