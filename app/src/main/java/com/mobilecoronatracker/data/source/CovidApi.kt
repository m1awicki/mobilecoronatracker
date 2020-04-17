package com.mobilecoronatracker.data.source

import com.mobilecoronatracker.model.pojo.Timeline
import com.mobilecoronatracker.model.pojo.CovidCountryEntry
import com.mobilecoronatracker.model.pojo.CovidCountryHistory
import com.mobilecoronatracker.model.pojo.CovidCumulatedData
import retrofit2.http.GET


interface CovidApi {
    @GET("v2/countries")
    suspend fun getCountriesData(): List<CovidCountryEntry>

    @GET("v2/all")
    suspend fun getAccumulatedData(): CovidCumulatedData

    @GET("v2/historical?lastdays=all")
    suspend fun getCountryHistoricalData(): List<CovidCountryHistory>

    @GET("v2/historical/all?lastdays=all")
    suspend fun getAccumulatedHistoricalData(): Timeline
}
