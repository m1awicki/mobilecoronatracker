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
    abstract fun getByTimestampFlow(timestamp: Long): Flow<AccumulatedData>

    @Query(
        """
        SELECT * 
        FROM accumulated_data 
        WHERE entry_date=:timestamp
        """
    )
    abstract suspend fun getByTimestamp(timestamp: Long): AccumulatedData?

    @Query(
        """
        SELECT * 
        FROM accumulated_data 
        WHERE entry_date>=:timestampStart AND entry_date<=:timestampEnd
        ORDER BY entry_date
        ASC
        """
    )
    abstract fun getByTimestampRangeFlow(
        timestampStart: Long,
        timestampEnd: Long
    ): Flow<List<AccumulatedData>>

    @Query(
        """
        SELECT COUNT(*)
        FROM accumulated_data
        """
    )
    abstract fun getRecordsCount() : Int

    @Query(
        """
        SELECT *
        FROM accumulated_data
        ORDER BY entry_date
        ASC
        """
    )
    abstract fun getAllFlow(): Flow<List<AccumulatedData>>
}
