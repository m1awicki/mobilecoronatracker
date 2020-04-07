package com.mobilecoronatracker.data.repository.impl

import com.mobilecoronatracker.data.networking.NetworkResult
import com.mobilecoronatracker.data.networking.safeApiCall
import com.mobilecoronatracker.data.repository.CovidDataRepo
import com.mobilecoronatracker.data.source.CovidApi
import com.mobilecoronatracker.model.CountryReportModelable
import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.model.impl.CountryReportModel
import com.mobilecoronatracker.model.impl.GeneralReportModel
import com.mobilecoronatracker.model.pojo.CovidAccumulatedHistory
import com.mobilecoronatracker.model.pojo.CovidCountryHistory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CovidDataRepoImpl(
    private val covidApi: CovidApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CovidDataRepo {
    override suspend fun getCountriesData(): List<CountryReportModelable> =
        when (val callResult = safeApiCall(dispatcher) { covidApi.getCountriesData() }) {
            is NetworkResult.ResponseResult -> callResult.data.map { CountryReportModel(it) }
            is NetworkResult.ErrorResult -> emptyList()

        }

    override suspend fun getCumulatedData(): GeneralReportModelable =
        when (val callResult = safeApiCall(dispatcher) { covidApi.getAccumulatedData() }) {
            is NetworkResult.ResponseResult -> GeneralReportModel(callResult.data)
            is NetworkResult.ErrorResult -> GeneralReportModel(0, 0, 0)
        }


    override suspend fun getCountryHistoricalData(): List<CovidCountryHistory> =
        when (val callResult = safeApiCall(dispatcher) { covidApi.getCountryHistoricalData() }) {
            is NetworkResult.ResponseResult -> callResult.data
            is NetworkResult.ErrorResult -> emptyList()
        }

    override suspend fun getAccumulatedHistoricalData(): CovidAccumulatedHistory =
        when (val callResult =
            safeApiCall(dispatcher) { covidApi.getAccumulatedHistoricalData() }) {
            is NetworkResult.ResponseResult -> CovidAccumulatedHistory(callResult.data)
            is NetworkResult.ErrorResult -> CovidAccumulatedHistory()
        }
}
