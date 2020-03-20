package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.data.source.CovidDataSource
import com.mobilecoronatracker.data.source.impl.CovidRestDataReader
import org.koin.dsl.module

val appModule = module {
    single { CovidRestDataReader() as CovidDataSource }
}