package com.mobilecoronatracker.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "country"
)
data class Country(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    @ColumnInfo(name = "code_iso2") val codeIso2: String,
    @ColumnInfo(name = "continent_name") val continentName: String = ""
)
