package com.mobilecoronatracker.data.repository

import com.mobilecoronatracker.model.GeneralReportModelable
import kotlinx.coroutines.flow.Flow

interface AccumulatedDataRepo {
    fun getTodayData(): Flow<GeneralReportModelable>
    suspend fun refreshAccumulatedData()
    suspend fun hasNoData(): Boolean
}
