package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.data.networking.RetrofitClientProvider
import com.mobilecoronatracker.data.networking.impl.RetrofitClientProviderImpl
import com.mobilecoronatracker.data.persistence.CountriesFollowRepo
import com.mobilecoronatracker.data.persistence.impl.SharedPreferencesCountriesFollowRepo
import com.mobilecoronatracker.data.source.CovidApi
import com.mobilecoronatracker.data.source.CovidDataRepo
import com.mobilecoronatracker.data.source.impl.CovidDataRepoImpl
import org.koin.dsl.module

val appModule = module {
    single<CountriesFollowRepo> { SharedPreferencesCountriesFollowRepo(get()) }
    single<RetrofitClientProvider> { RetrofitClientProviderImpl }
    single { get<RetrofitClientProvider>().getRetrofitClient().create(CovidApi::class.java) }
    single<CovidDataRepo> { CovidDataRepoImpl(get()) }
}
