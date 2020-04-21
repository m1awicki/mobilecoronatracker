package com.mobilecoronatracker.data.repository.impl

import com.mobilecoronatracker.data.persistence.dao.AccumulatedDataDao
import com.mobilecoronatracker.data.repository.AccumulatedDataRepo
import com.mobilecoronatracker.data.repository.CovidDataRepo
import com.mobilecoronatracker.data.repository.RepoResult
import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.model.GeneralReportTimePointModelable
import com.mobilecoronatracker.model.impl.GeneralReportModel
import com.mobilecoronatracker.model.impl.GeneralReportTimePointModel
import com.mobilecoronatracker.model.pojo.toAccumulatedDataList
import com.mobilecoronatracker.model.toAccumulatedData
import com.mobilecoronatracker.utils.getTimestampForDaysBefore
import com.mobilecoronatracker.utils.getTodayTimestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class AccumulatedDataRepoRoomImpl(
    private val accumulatedDataDao: AccumulatedDataDao,
    private val covidDataRepo: CovidDataRepo
) : AccumulatedDataRepo {
    override fun getTodayData(): Flow<GeneralReportModelable> {
        val todayTimestamp = getTodayTimestamp()
        return accumulatedDataDao.getByTimestampFlow(todayTimestamp).filterNotNull().map { data ->
            GeneralReportModel(data)
        }
    }

    override fun getFullHistory(): Flow<List<GeneralReportTimePointModelable>> {
        return accumulatedDataDao.getAllFlow().map {
            it.map { data -> GeneralReportTimePointModel(data) }
        }
    }

    override fun getDataForLastDays(numberOfDays: Int): Flow<List<GeneralReportModelable>> {
        val upperBound = getTodayTimestamp()
        val lowerBound = getTimestampForDaysBefore(numberOfDays - 1, upperBound)
        return accumulatedDataDao.getByTimestampRangeFlow(lowerBound, upperBound).map {
            it.map { data -> GeneralReportModel(data) }
        }
    }

    override suspend fun fetchTodayAccumulatedData(): RepoResult<Unit> =
        when (val repoResult = covidDataRepo.getCumulatedData()) {
            is RepoResult.FailureResult -> RepoResult.FailureResult(repoResult.throwable)
            is RepoResult.SuccessResult -> {
                val todayTimestamp = getTodayTimestamp()
                insertOrUpdate(todayTimestamp, repoResult.data)
                RepoResult.SuccessResult(Unit)
            }
        }


    override suspend fun fetchHistoricalAccumulatedData() {
        val accumulatedHistorical = covidDataRepo.getAccumulatedHistoricalData()
        val accumulatedDataList = accumulatedHistorical.timeline.toAccumulatedDataList()
        accumulatedDataDao.insert(*accumulatedDataList.toTypedArray())
    }

    private suspend fun insertOrUpdate(
        timestamp: Long,
        generalReportModelable: GeneralReportModelable
    ) {
        val accumulatedData = accumulatedDataDao.getByTimestamp(timestamp)
        if (accumulatedData == null) {
            accumulatedDataDao.insert(
                generalReportModelable.toAccumulatedData(0, timestamp)
            )
        } else {
            accumulatedDataDao.update(
                generalReportModelable.toAccumulatedData(
                    accumulatedData.id,
                    accumulatedData.entryDate
                )
            )
        }
    }

    override suspend fun hasTodayData(): Boolean =
        accumulatedDataDao.getByTimestamp(getTodayTimestamp()) != null

    override suspend fun hasAnyData(): Boolean = accumulatedDataDao.getRecordsCount() > 0
}
