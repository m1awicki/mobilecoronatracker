package com.mobilecoronatracker.data.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import com.mobilecoronatracker.data.persistence.entity.ProvinceData
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProvinceDataDao : BaseDao<ProvinceData> {
    @Query(
        """
        SELECT * 
        FROM province_data 
        WHERE entry_date=:timestamp
        """
    )
    abstract fun getAllProvincesByTimestampFlow(timestamp: Long): Flow<List<ProvinceData>>
}
