package com.mobilecoronatracker.data.persistence

import com.mobilecoronatracker.data.persistence.dao.AccumulatedDataDao
import com.mobilecoronatracker.data.persistence.dao.CountryDao
import com.mobilecoronatracker.data.persistence.dao.CountryDataDao
import com.mobilecoronatracker.data.persistence.dao.ProvinceDao
import com.mobilecoronatracker.data.persistence.dao.ProvinceDataDao

interface CoronaTrackerDatabase {
    suspend fun <R> withTransactionWrapper(block: suspend () -> R)
    fun provinceDao(): ProvinceDao
    fun provinceDataDao(): ProvinceDataDao
    fun countryDao(): CountryDao
    fun countryDataDao(): CountryDataDao
    fun accumulatedDataDao(): AccumulatedDataDao
}