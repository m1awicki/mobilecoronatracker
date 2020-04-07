package com.mobilecoronatracker.data.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import com.mobilecoronatracker.data.persistence.entity.Country

@Dao
abstract class CountryDao : BaseDao<Country> {
    @Query(
        """
        SELECT *
        FROM country
        WHERE name=:countryName
        """
    )
    abstract suspend fun getByCountryName(countryName: String): Country?

    @Query(
        """
        SELECT *
        FROM country
        """
    )
    abstract suspend fun getAllCountries(): List<Country>
}
