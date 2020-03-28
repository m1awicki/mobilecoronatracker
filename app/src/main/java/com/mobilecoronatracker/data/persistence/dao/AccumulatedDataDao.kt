package com.mobilecoronatracker.data.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import com.mobilecoronatracker.data.persistence.entity.AccumulatedData
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AccumulatedDataDao : BaseDao<AccumulatedData> {
    @Query(
        """
        SELECT * 
        FROM accumulated_data 
        WHERE entry_date=:timestamp
        """
    )
    abstract fun getByTimestamp(timestamp: Long): Flow<AccumulatedData>

    @Query(
        """
        SELECT * 
        FROM accumulated_data 
        WHERE entry_date=:timestamp
        """
    )
    abstract suspend fun getByTimestampNow(timestamp: Long): AccumulatedData?

    @Query(
        """
        SELECT * 
        FROM accumulated_data 
        WHERE entry_date>=:timestampStart AND entry_date<=:timestampEnd
        """
    )
    abstract fun getByTimestampRange(
        timestampStart: Long,
        timestampEnd: Long
    ): Flow<AccumulatedData>
}
