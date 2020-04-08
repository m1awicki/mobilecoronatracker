package com.mobilecoronatracker.data.repository

import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.model.GeneralReportTimePointModelable
import kotlinx.coroutines.flow.Flow

interface AccumulatedDataRepo {
    fun getTodayData(): Flow<GeneralReportModelable>
    fun getFullHistory(): Flow<List<GeneralReportTimePointModelable>>
    suspend fun refreshAccumulatedData()
    suspend fun hasTodayData(): Boolean
    suspend fun hasAnyData(): Boolean
}
