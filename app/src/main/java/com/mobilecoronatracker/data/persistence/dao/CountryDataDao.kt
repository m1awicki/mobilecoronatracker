package com.mobilecoronatracker.data.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import com.mobilecoronatracker.data.persistence.entity.CountryData
import com.mobilecoronatracker.data.persistence.entity.CountryDataWithCountryName
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CountryDataDao : BaseDao<CountryData> {
    @Query(
        """
        SELECT country_data.*, country.name 
        FROM country_data 
        INNER JOIN country ON country_data.country_id = country.id 
        WHERE entry_date=:timestamp 
        ORDER BY infected DESC
        """
    )
    abstract fun getAllCountryByTimestamp(timestamp: Long): Flow<List<CountryDataWithCountryName>>

    @Query(
        """
        SELECT country_data.*, country.name 
        FROM country_data 
        INNER JOIN country ON country_data.country_id = country.id 
        WHERE entry_date=:timestamp 
        ORDER BY infected DESC
        """
    )
    abstract suspend fun getAllCountryByTimestampNow(timestamp: Long): List<CountryDataWithCountryName>

    @Query(
        """
        SELECT * 
        FROM country_data 
        WHERE entry_date>=:timestampStart AND entry_date<=:timestampEnd
        """
    )
    abstract fun getCountryByTimestampRange(
        timestampStart: Long,
        timestampEnd: Long
    ): Flow<List<CountryData>>
}
