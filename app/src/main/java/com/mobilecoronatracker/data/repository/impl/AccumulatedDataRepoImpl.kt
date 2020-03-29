package com.mobilecoronatracker.data.repository.impl

import com.mobilecoronatracker.data.persistence.dao.AccumulatedDataDao
import com.mobilecoronatracker.data.persistence.entity.AccumulatedData
import com.mobilecoronatracker.data.repository.AccumulatedDataRepo
import com.mobilecoronatracker.data.repository.CovidDataRepo
import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.model.impl.GeneralReportModel
import com.mobilecoronatracker.utils.getTodayTimestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class AccumulatedDataRepoImpl(
    private val accumulatedDataDao: AccumulatedDataDao,
    private val covidDataRepo: CovidDataRepo
) : AccumulatedDataRepo {
    override fun getTodayData(): Flow<GeneralReportModelable> {
        val todayTimestamp = getTodayTimestamp()
        return accumulatedDataDao.getByTimestampFlow(todayTimestamp).filterNotNull().map { data ->
            GeneralReportModel(data)
        }
    }

    override suspend fun refreshAccumulatedData() {
        val generalReportModelable = covidDataRepo.getCumulatedData()
        val todayTimestamp = getTodayTimestamp()

        insertOrUpdate(todayTimestamp, generalReportModelable)
    }

    private suspend fun insertOrUpdate(
        todayTimestamp: Long,
        generalReportModelable: GeneralReportModelable
    ) {
        val accumulatedData = accumulatedDataDao.getByTimestamp(todayTimestamp)
        if (accumulatedData == null) {
            accumulatedDataDao.insert(
                AccumulatedData(
                    0,
                    generalReportModelable.cases,
                    generalReportModelable.deaths,
                    generalReportModelable.recovered,
                    todayTimestamp
                )
            )
        } else {
            accumulatedDataDao.update(
                accumulatedData.copy(
                    infected = generalReportModelable.cases,
                    dead = generalReportModelable.deaths,
                    recovered = generalReportModelable.recovered
                )
            )
        }
    }

    override suspend fun hasNoData(): Boolean =
        accumulatedDataDao.getByTimestamp(getTodayTimestamp()) == null
}
