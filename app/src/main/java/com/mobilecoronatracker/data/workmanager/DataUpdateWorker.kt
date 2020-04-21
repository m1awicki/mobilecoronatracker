package com.mobilecoronatracker.data.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mobilecoronatracker.data.repository.AccumulatedDataRepo
import com.mobilecoronatracker.data.repository.CountriesDataRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class DataUpdateWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {
    private val accumulatedDataRepo: AccumulatedDataRepo by inject()
    private val countriesDataRepo: CountriesDataRepo by inject()

    override suspend fun doWork(): Result = coroutineScope {

        // TODO: add yesterday call + check status of sync somehow to pass retry
        withContext(Dispatchers.IO) {
            accumulatedDataRepo.fetchTodayAccumulatedData()
            countriesDataRepo.fetchTodayCountriesData()
        }

        Result.success()
    }
}