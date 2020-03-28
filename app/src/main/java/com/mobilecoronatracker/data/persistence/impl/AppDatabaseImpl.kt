package com.mobilecoronatracker.data.persistence.impl

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.mobilecoronatracker.data.persistence.AppDatabase
import com.mobilecoronatracker.data.persistence.dao.AccumulatedDataDao
import com.mobilecoronatracker.data.persistence.dao.CountryDao
import com.mobilecoronatracker.data.persistence.dao.CountryDataDao
import com.mobilecoronatracker.data.persistence.dao.ProvinceDao
import com.mobilecoronatracker.data.persistence.dao.ProvinceDataDao
import com.mobilecoronatracker.data.persistence.entity.AccumulatedData
import com.mobilecoronatracker.data.persistence.entity.Country
import com.mobilecoronatracker.data.persistence.entity.CountryData
import com.mobilecoronatracker.data.persistence.entity.Province
import com.mobilecoronatracker.data.persistence.entity.ProvinceData

@Database(
    entities = [
        AccumulatedData::class,
        CountryData::class,
        Country::class,
        Province::class,
        ProvinceData::class
    ],
    version = 1
)
abstract class AppDatabaseImpl : RoomDatabase(), AppDatabase {
    abstract override fun accumulatedDataDao(): AccumulatedDataDao
    abstract override fun countryDataDao(): CountryDataDao
    abstract override fun countryDao(): CountryDao
    abstract override fun provinceDao(): ProvinceDao
    abstract override fun provinceDataDao(): ProvinceDataDao

    override suspend fun <R> withTransactionWrapper(block: suspend () -> R) {
        withTransaction {
            block()
        }
    }

    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabaseImpl::class.java, "covid_db")
                .build()
    }
}