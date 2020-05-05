package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.data.persistence.CoronaTrackerDatabase
import com.mobilecoronatracker.data.persistence.CoronaTrackerRoomDatabase
import org.koin.dsl.module

val dbModule = module {
    single { CoronaTrackerRoomDatabase.buildDatabase(get()) }
    single<CoronaTrackerDatabase> { get<CoronaTrackerRoomDatabase>() }
    single { get<CoronaTrackerRoomDatabase>().accumulatedDataDao() }
    single { get<CoronaTrackerRoomDatabase>().countryDao() }
    single { get<CoronaTrackerRoomDatabase>().countryDataDao() }
    single { get<CoronaTrackerRoomDatabase>().provinceDao() }
    single { get<CoronaTrackerRoomDatabase>().provinceDataDao() }
}
