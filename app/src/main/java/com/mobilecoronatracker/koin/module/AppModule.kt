package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.data.networking.RetrofitClientProvider
import com.mobilecoronatracker.data.networking.impl.RetrofitClientProviderImpl
import com.mobilecoronatracker.data.repository.AccumulatedDataRepo
import com.mobilecoronatracker.data.repository.CountriesDataRepo
import com.mobilecoronatracker.data.repository.CountriesFollowRepo
import com.mobilecoronatracker.data.repository.CovidDataRepo
import com.mobilecoronatracker.data.repository.RepoInitStrategy
import com.mobilecoronatracker.data.repository.RepoInitializer
import com.mobilecoronatracker.data.repository.impl.AccumulatedDataRepoRoomImpl
import com.mobilecoronatracker.data.repository.impl.CountriesDataRepoRoomImpl
import com.mobilecoronatracker.data.repository.impl.CovidDataRepoImpl
import com.mobilecoronatracker.data.repository.impl.CovidOnlyInitStrategy
import com.mobilecoronatracker.data.repository.impl.RepoInitializerImpl
import com.mobilecoronatracker.data.repository.impl.SharedPreferencesCountriesFollowRepo
import com.mobilecoronatracker.data.source.CovidApi
import org.koin.dsl.module

val appModule = module {
    single<CountriesFollowRepo> {
        SharedPreferencesCountriesFollowRepo(
            get()
        )
    }
    single<CountriesDataRepo> { CountriesDataRepoRoomImpl(get(), get(), get(), get()) }
    single<AccumulatedDataRepo> { AccumulatedDataRepoRoomImpl(get(), get()) }
    single<RetrofitClientProvider> { RetrofitClientProviderImpl }
    single { get<RetrofitClientProvider>().getRetrofitClient().create(CovidApi::class.java) }
    single<CovidDataRepo> {
        CovidDataRepoImpl(
            get()
        )
    }
    factory<RepoInitStrategy> { CovidOnlyInitStrategy(get(), get()) }
    factory<RepoInitializer> { RepoInitializerImpl(get()) }
}
