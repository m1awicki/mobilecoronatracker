package com.mobilecoronatracker.data.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mobilecoronatracker.data.repository.AccumulatedDataRepo
import com.mobilecoronatracker.data.repository.CountriesDataRepo
import com.mobilecoronatracker.data.repository.RepoResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DataUpdateWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {
    private val accumulatedDataRepo: AccumulatedDataRepo by inject()
    private val countriesDataRepo: CountriesDataRepo by inject()

    override suspend fun doWork(): Result = coroutineScope {
        var todayAccumulatedSuccessful: RepoResult<Unit>
        var yesterdayAccumulatedSuccessful: RepoResult<Unit>
        var todayCountriesSuccessful: RepoResult<Unit>
        var yesterdayCountriesSuccessful: RepoResult<Unit>
        withContext(Dispatchers.IO) {
            todayAccumulatedSuccessful = accumulatedDataRepo.fetchTodayAccumulatedData()
            yesterdayAccumulatedSuccessful = accumulatedDataRepo.fetchYesterdayAccumulatedData()
            todayCountriesSuccessful = countriesDataRepo.fetchTodayCountriesData()
            yesterdayCountriesSuccessful = countriesDataRepo.fetchYesterdayCountriesData()
        }

        if (todayAccumulatedSuccessful is RepoResult.SuccessResult
            && todayCountriesSuccessful is RepoResult.SuccessResult
            && yesterdayCountriesSuccessful is RepoResult.SuccessResult
            && yesterdayAccumulatedSuccessful is RepoResult.SuccessResult
        ) {
            return@coroutineScope Result.success()
        }

        Result.retry()
    }
}