package com.mobilecoronatracker.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class CountryDataWithCountryInfo(
    @Embedded val countryData: CountryData,
    @ColumnInfo(name = "name") val countryName: String,
    @ColumnInfo(name = "iso2") val iso2: String,
    @ColumnInfo(name = "country_flag_path") val flagPath: String
)
