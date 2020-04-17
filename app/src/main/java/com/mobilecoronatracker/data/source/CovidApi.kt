package com.mobilecoronatracker.data.source

import com.mobilecoronatracker.model.pojo.CovidCountryEntry
import com.mobilecoronatracker.model.pojo.CovidCumulatedData
import retrofit2.http.GET


interface CovidApi {
    @GET("v2/countries")
    suspend fun getCountriesData(): List<CovidCountryEntry>

    @GET("v2/all")
    suspend fun getCumulatedData(): CovidCumulatedData
}
