package com.mobilecoronatracker.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "country_data",
    indices = [
        Index(value = ["country_id", "entry_date"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = Country::class,
            parentColumns = ["id"],
            childColumns = ["country_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class CountryData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cases: Int = 0,
    @ColumnInfo(name = "today_cases") val todayCases: Int = 0,
    val deaths: Int = 0,
    @ColumnInfo(name = "today_deaths") val todayDeaths: Int = 0,
    val recovered: Int = 0,
    val critical: Int = 0,
    @ColumnInfo(name = "cases_per_million") val casesPerMillion: Double = 0.0,
    @ColumnInfo(name = "deaths_per_million") val deathsPerMillion: Double = 0.0,
    @ColumnInfo(name = "tests") val tests: Int = 0,
    @ColumnInfo(name = "tests_per_million") val testsPerMillion: Double = 0.0,
    @ColumnInfo(name = "entry_date") val entryDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "country_id") val countryId: Long
)
