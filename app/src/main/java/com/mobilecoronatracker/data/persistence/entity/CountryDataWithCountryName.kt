package com.mobilecoronatracker.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class CountryDataWithCountryName(
    @Embedded val countryData: CountryData,
    @ColumnInfo(name = "name") val countryName: String
)
