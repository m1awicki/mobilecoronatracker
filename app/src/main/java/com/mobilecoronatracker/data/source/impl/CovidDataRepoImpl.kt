package com.mobilecoronatracker.data.source.impl

import com.mobilecoronatracker.data.networking.NetworkResult
import com.mobilecoronatracker.data.networking.safeApiCall
import com.mobilecoronatracker.data.source.CovidApi
import com.mobilecoronatracker.data.source.CovidDataRepo
import com.mobilecoronatracker.model.CountryReportModelable
import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.model.impl.CountryReportModel
import com.mobilecoronatracker.model.impl.GeneralReportModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CovidDataRepoImpl(
    private val covidApi: CovidApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CovidDataRepo {
    override suspend fun getCountriesData(): List<CountryReportModelable> {
        return when (val callResult = safeApiCall(dispatcher) { covidApi.getCountriesData() }) {
            is NetworkResult.ResponseResult -> callResult.data.map { CountryReportModel(it) }
            is NetworkResult.ErrorResult -> emptyList()
        }
    }

    override suspend fun getCumulatedData(): GeneralReportModelable {
        return when (val callResult = safeApiCall(dispatcher) { covidApi.getCumulatedData() }) {
            is NetworkResult.ResponseResult -> GeneralReportModel(callResult.data)
            is NetworkResult.ErrorResult -> GeneralReportModel(0, 0, 0)
        }
    }
}
