package com.mobilecoronatracker.data.repository.impl

import android.util.Log
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
        Log.d("MDMD", "$todayTimestamp")
        return accumulatedDataDao.getByTimestamp(todayTimestamp).filterNotNull().map { data ->
            GeneralReportModel(data)
        }
    }

    override suspend fun refreshAccumulatedData() {
        val accumulatedData = covidDataRepo.getCumulatedData()
        val todayTimestamp = getTodayTimestamp()
        accumulatedDataDao.insert(
            AccumulatedData(
                0,
                accumulatedData.cases,
                accumulatedData.deaths,
                accumulatedData.recovered,
                getTodayTimestamp()
            )
        )
    }

    override suspend fun hasNoData(): Boolean =
        accumulatedDataDao.getByTimestampNow(getTodayTimestamp()) == null
}
