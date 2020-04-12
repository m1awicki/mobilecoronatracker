package com.mobilecoronatracker.data.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import com.mobilecoronatracker.data.persistence.entity.CountryData
import com.mobilecoronatracker.data.persistence.entity.CountryDataWithCountryInfo
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CountryDataDao : BaseDao<CountryData> {
    @Query(
        """
        SELECT country_data.*, country.name, country.iso2, country.country_flag_path
        FROM country_data 
        INNER JOIN country ON country_data.country_id = country.id 
        WHERE entry_date=:timestamp 
        ORDER BY cases DESC
        """
    )
    abstract fun getAllCountriesByTimestampFlow(timestamp: Long): Flow<List<CountryDataWithCountryInfo>>

    @Query(
        """
        SELECT COUNT(*)
        FROM country_data 
        WHERE entry_date=:timestamp
        """
    )
    abstract suspend fun getAllCountriesCountByTimestamp(timestamp: Long): Int

    @Query(
        """
        SELECT * 
        FROM country_data 
        WHERE entry_date>=:timestampStart AND entry_date<=:timestampEnd AND country_id=:countryId
        """
    )
    abstract fun getCountryDataForTimeRange(
        timestampStart: Long,
        timestampEnd: Long,
        countryId: Long
    ): Flow<List<CountryData>>

    @Query(
        """
        SELECT * 
        FROM country_data 
        WHERE entry_date = :todayTimestamp AND country_id = :countryId
        """
    )
    abstract fun getCountryByTimestamp(countryId: Long, todayTimestamp: Long): CountryData?
}
