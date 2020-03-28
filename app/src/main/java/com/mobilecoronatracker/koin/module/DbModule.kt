package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.data.persistence.AppDatabase
import com.mobilecoronatracker.data.persistence.impl.AppDatabaseImpl
import org.koin.dsl.module

val dbModule = module {
    single { AppDatabaseImpl.buildDatabase(get()) }
    single<AppDatabase> { get<AppDatabaseImpl>() }
    single { get<AppDatabaseImpl>().accumulatedDataDao() }
    single { get<AppDatabaseImpl>().countryDao() }
    single { get<AppDatabaseImpl>().countryDataDao() }
    single { get<AppDatabaseImpl>().provinceDao() }
    single { get<AppDatabaseImpl>().provinceDataDao() }
}
