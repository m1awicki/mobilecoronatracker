package com.mobilecoronatracker.data.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class DataUpdateWorkSchedulerImpl : DataUpdateWorkScheduler {
    override fun schedule(context: Context) {
        val constraints: Constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val dataUpdateWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<DataUpdateWorker>(
                DATA_UPDATE_WORK_REPEAT_INTERVAL,
                TimeUnit.HOURS
            )
                .setInitialDelay(DATA_UPDATE_WORK_INITIAL_DELAY, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                DATA_UPDATE_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                dataUpdateWorkRequest
            )
    }

    companion object {
        const val DATA_UPDATE_WORK_NAME = "get_latest_data_work"
        const val DATA_UPDATE_WORK_REPEAT_INTERVAL = 6L
        const val DATA_UPDATE_WORK_INITIAL_DELAY = 1L
    }
}
