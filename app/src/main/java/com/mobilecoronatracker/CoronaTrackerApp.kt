package com.mobilecoronatracker

import android.app.Application
import com.facebook.stetho.Stetho
import com.mobilecoronatracker.koin.module.appModule
import com.mobilecoronatracker.koin.module.countriesListModule
import com.mobilecoronatracker.koin.module.countryAnalysisModule
import com.mobilecoronatracker.koin.module.cumulatedReportModule
import com.mobilecoronatracker.koin.module.dbModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CoronaTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
        startKoin {
            androidLogger()
            androidContext(this@CoronaTrackerApp)
            modules(
                appModule,
                countriesListModule,
                countryAnalysisModule,
                cumulatedReportModule,
                dbModule
            )
        }
    }
}
