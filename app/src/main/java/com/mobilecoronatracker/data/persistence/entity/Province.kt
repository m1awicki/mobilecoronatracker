package com.mobilecoronatracker.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "province",
    indices = [Index("name", "country_id", unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Country::class,
            parentColumns = ["id"],
            childColumns = ["country_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Province(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    @ColumnInfo(name = "country_id") val countryId: Long
)
