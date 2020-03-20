package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.data.persistence.CountriesFollowRepo
import com.mobilecoronatracker.data.persistence.impl.SharedPreferencesCountriesFollowRepo
import com.mobilecoronatracker.data.source.CovidDataSource
import com.mobilecoronatracker.data.source.impl.CovidRestDataReader
import org.koin.dsl.module

val appModule = module {
    single<CovidDataSource> { CovidRestDataReader() }
    single<CountriesFollowRepo> { SharedPreferencesCountriesFollowRepo(get()) }
}
