package com.mobilecoronatracker.data.repository

import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.model.GeneralReportTimePointModelable
import kotlinx.coroutines.flow.Flow

interface AccumulatedDataRepo {
    fun getTodayData(): Flow<GeneralReportModelable>
    fun getFullHistory(): Flow<List<GeneralReportTimePointModelable>>
    fun getDataForLastDays(numberOfDays: Int): Flow<List<GeneralReportModelable>>
    suspend fun fetchTodayAccumulatedData()
    suspend fun hasTodayData(): Boolean
    suspend fun hasAnyData(): Boolean
    suspend fun fetchHistoricalAccumulatedData()
}
