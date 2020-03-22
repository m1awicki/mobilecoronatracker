package com.mobilecoronatracker.data.source

import com.mobilecoronatracker.model.pojo.CovidCountryEntry
import com.mobilecoronatracker.model.pojo.CovidCumulatedData
import retrofit2.http.GET


interface CovidApi {
    @GET("countries")
    suspend fun getCountriesData(): List<CovidCountryEntry>

    @GET("all")
    suspend fun getCumulatedData(): CovidCumulatedData
}