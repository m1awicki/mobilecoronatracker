package com.mobilecoronatracker

import android.app.Application
import com.mobilecoronatracker.koin.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CoronaTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CoronaTrackerApp)
            modules(appModule)
        }
    }
}