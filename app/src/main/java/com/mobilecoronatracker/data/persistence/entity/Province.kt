package com.mobilecoronatracker.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "province",
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
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    @ColumnInfo(name = "country_id") val countryId: Int
)
