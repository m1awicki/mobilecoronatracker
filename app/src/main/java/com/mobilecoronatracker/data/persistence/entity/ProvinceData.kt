package com.mobilecoronatracker.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "province_data",
    indices = [
        Index(value = ["id", "entry_date"], unique = true)
    ],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Province::class,
            parentColumns = ["id"],
            childColumns = ["province_id"],
            onDelete = androidx.room.ForeignKey.CASCADE,
            onUpdate = androidx.room.ForeignKey.CASCADE
        )
    ]
)
class ProvinceData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val infected: Int = 0,
    val dead: Int = 0,
    val recovered: Int = 0,
    @ColumnInfo(name = "entry_date") val entryDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "province_id") val provinceId: Int
)
