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
import org.koin.core.KoinComponent
import org.koin.core.inject

class DataUpdateWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {
    private val accumulatedDataRepo: AccumulatedDataRepo by inject()
    private val countriesDataRepo: CountriesDataRepo by inject()

    override suspend fun doWork(): Result = coroutineScope {
        var accumulatedSuccessful: RepoResult<Unit> = RepoResult.FailureResult()
        var todayCountriesSuccessful: RepoResult<Unit> = RepoResult.FailureResult()
        var yesterdayCountriesSuccessful: RepoResult<Unit> = RepoResult.FailureResult()
        withContext(Dispatchers.IO) {
            accumulatedSuccessful = accumulatedDataRepo.fetchTodayAccumulatedData()
            todayCountriesSuccessful = countriesDataRepo.fetchTodayCountriesData()
            yesterdayCountriesSuccessful = countriesDataRepo.fetchYesterdayCountriesData()
        }

        if (accumulatedSuccessful is RepoResult.SuccessResult
            && todayCountriesSuccessful is RepoResult.SuccessResult
            && yesterdayCountriesSuccessful is RepoResult.SuccessResult
        ) {
            return@coroutineScope Result.success()
        }

        Result.retry()
    }
}