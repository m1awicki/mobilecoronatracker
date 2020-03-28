package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.data.networking.RetrofitClientProvider
import com.mobilecoronatracker.data.networking.impl.RetrofitClientProviderImpl
import com.mobilecoronatracker.data.repository.AccumulatedDataRepo
import com.mobilecoronatracker.data.repository.CountriesDataRepo
import com.mobilecoronatracker.data.repository.CountriesFollowRepo
import com.mobilecoronatracker.data.repository.CovidDataRepo
import com.mobilecoronatracker.data.repository.impl.AccumulatedDataRepoImpl
import com.mobilecoronatracker.data.repository.impl.CountriesDataRepoImpl
import com.mobilecoronatracker.data.repository.impl.CovidDataRepoImpl
import com.mobilecoronatracker.data.repository.impl.SharedPreferencesCountriesFollowRepo
import com.mobilecoronatracker.data.source.CovidApi
import org.koin.dsl.module

val appModule = module {
    single<CountriesFollowRepo> {
        SharedPreferencesCountriesFollowRepo(
            get()
        )
    }
    single<CountriesDataRepo> { CountriesDataRepoImpl(get(), get(), get(), get()) }
    single<AccumulatedDataRepo> { AccumulatedDataRepoImpl(get(), get()) }
    single<RetrofitClientProvider> { RetrofitClientProviderImpl }
    single { get<RetrofitClientProvider>().getRetrofitClient().create(CovidApi::class.java) }
    single<CovidDataRepo> {
        CovidDataRepoImpl(
            get()
        )
    }
}
