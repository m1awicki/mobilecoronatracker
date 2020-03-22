package com.mobilecoronatracker.data.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class AccumulatedDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val infected: Int = 0,
    val dead: Int = 0,
    val recovered: Int = 0,
    val entryDate: Long = System.currentTimeMillis()
)
