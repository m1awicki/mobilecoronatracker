package com.mobilecoronatracker.data.source

import com.mobilecoronatracker.model.pojo.CovidCountryEntry
import com.mobilecoronatracker.model.pojo.CovidCountryHistory
import com.mobilecoronatracker.model.pojo.CovidCumulatedData
import com.mobilecoronatracker.model.pojo.Timeline
import retrofit2.http.GET
import retrofit2.http.Query


interface CovidApi {
    @GET("countries")
    suspend fun getCountriesData(@Query("yesterday") yesterday: Boolean = false): List<CovidCountryEntry>

    @GET("all")
    suspend fun getAccumulatedData(@Query("yesterday") yesterday: Boolean = false): CovidCumulatedData

    @GET("historical?lastdays=all")
    suspend fun getCountryHistoricalData(): List<CovidCountryHistory>

    @GET("historical/all?lastdays=all")
    suspend fun getAccumulatedHistoricalData(): Timeline
}
