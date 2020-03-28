package com.mobilecoronatracker.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "accumulated_data",
    indices = [
        Index(value = ["entry_date"], unique = true)
    ]
)
data class AccumulatedData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val infected: Int = 0,
    val dead: Int = 0,
    val recovered: Int = 0,
    @ColumnInfo(name = "entry_date") val entryDate: Long = System.currentTimeMillis()
)
