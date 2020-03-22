package com.mobilecoronatracker.data.persistence.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    indices = [
        Index(value = arrayOf("id", "entryDate"), unique = true)
    ]
)
class RegionDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val infected: Int = 0,
    val dead: Int = 0,
    val recovered: Int = 0,
    val entryDate: Long = System.currentTimeMillis(),
    val regionId: Int
)
