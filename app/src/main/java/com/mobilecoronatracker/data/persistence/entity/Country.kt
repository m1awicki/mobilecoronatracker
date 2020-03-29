package com.mobilecoronatracker.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "country",
    indices = [Index(value = ["name"], unique = true)]
)
data class Country(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    @ColumnInfo(name = "iso2") val iso2: String,
    @ColumnInfo(name = "continent_name") val continentName: String = ""
)
