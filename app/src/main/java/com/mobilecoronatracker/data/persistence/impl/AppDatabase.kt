package com.mobilecoronatracker.data.persistence.impl

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobilecoronatracker.data.persistence.dao.AccumulatedDataDao
import com.mobilecoronatracker.data.persistence.dao.CountryDao
import com.mobilecoronatracker.data.persistence.dao.CountryDataDao
import com.mobilecoronatracker.data.persistence.dao.ProvinceDao
import com.mobilecoronatracker.data.persistence.dao.ProvinceDataDao
import com.mobilecoronatracker.data.persistence.entity.AccumulatedData
import com.mobilecoronatracker.data.persistence.entity.CountryData
import com.mobilecoronatracker.data.persistence.entity.Country
import com.mobilecoronatracker.data.persistence.entity.ProvinceData
import com.mobilecoronatracker.data.persistence.entity.Province

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
abstract class AppDatabase : RoomDatabase() {
    abstract fun accumulatedDataDao(): AccumulatedDataDao
    abstract fun countryDataDao(): CountryDataDao
    abstract fun countryDao(): CountryDao
    abstract fun provinceDao(): ProvinceDao
    abstract fun provinceDataDao(): ProvinceDataDao

    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "covid_db")
                .build()
    }
}