package com.mobilecoronatracker.data.source.impl

import com.mobilecoronatracker.data.networking.NetworkResult
import com.mobilecoronatracker.data.networking.safeApiCall
import com.mobilecoronatracker.data.source.CovidApi
import com.mobilecoronatracker.data.source.CovidDataRepo
import com.mobilecoronatracker.model.CountryReportModel
import com.mobilecoronatracker.model.CountryReportModelable
import com.mobilecoronatracker.model.GeneralReportModel
import com.mobilecoronatracker.model.GeneralReportModelable

class CovidDataRepoImpl(private val covidApi: CovidApi) : CovidDataRepo {
    override suspend fun getCountriesData(): List<CountryReportModelable> {
        return when (val safeApiCallResult =
            safeApiCall { covidApi.getCountriesData() }) {
            is NetworkResult.ResponseResult -> safeApiCallResult.data.map { CountryReportModel(it) }
            is NetworkResult.ErrorResult -> emptyList()
        }
    }

    override suspend fun getCumulatedData(): GeneralReportModelable {
        return when (val safeApiCallResult =
            safeApiCall { covidApi.getCumulatedData() }) {
            is NetworkResult.ResponseResult -> GeneralReportModel(safeApiCallResult.data)
            is NetworkResult.ErrorResult -> GeneralReportModel(0, 0, 0)
        }
    }
}
