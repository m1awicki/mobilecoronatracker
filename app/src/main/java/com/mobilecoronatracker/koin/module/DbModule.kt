package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.data.persistence.impl.AppDatabase
import org.koin.dsl.module

val dbModule = module {
    single { AppDatabase.buildDatabase(get()) }
    single { get<AppDatabase>().accumulatedDataDao() }
    single { get<AppDatabase>().countryDao() }
    single { get<AppDatabase>().countryDataDao() }
    single { get<AppDatabase>().provinceDao() }
    single { get<AppDatabase>().provinceDataDao() }
}
