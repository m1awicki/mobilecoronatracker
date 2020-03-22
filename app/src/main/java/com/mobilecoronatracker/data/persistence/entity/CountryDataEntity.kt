package com.mobilecoronatracker.data.persistence.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    indices = [
        Index(value = arrayOf("countryId", "entryDate"), unique = true)
    ]
)
data class CountryDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val infected: Int = 0,
    val todayInfected: Int = 0,
    val dead: Int = 0,
    val todayDead: Int = 0,
    val recovered: Int = 0,
    val critical: Int = 0,
    val active: Int = 0,
    val entryDate: Long = System.currentTimeMillis(),
    val countryId: Int
)
