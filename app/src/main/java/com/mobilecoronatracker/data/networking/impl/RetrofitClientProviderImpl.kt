package com.mobilecoronatracker.data.networking.impl

import com.mobilecoronatracker.data.networking.RetrofitClientProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientProviderImpl :
    RetrofitClientProvider {
    override fun getRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private const val BASE_URL = "https://disease.sh/v3/covid-19/"
}
