package com.mobilecoronatracker.data.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class CountryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val continent_name: String
)
