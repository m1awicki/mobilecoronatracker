package com.mobilecoronatracker.data.networking

import retrofit2.Retrofit

interface RetrofitClientProvider {
    fun getRetrofitClient(): Retrofit
}
