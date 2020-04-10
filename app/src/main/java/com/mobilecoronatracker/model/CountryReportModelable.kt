package com.mobilecoronatracker.model

import com.mobilecoronatracker.data.persistence.entity.CountryData

interface CountryReportModelable {
    val country: String
    val iso2: String
    val flagPath: String
    val cases: Int
    val todayCases: Int
    val deaths: Int
    val todayDeaths: Int
    val recovered: Int
    val critical: Int
    val active: Int
    var followed: Boolean
}

fun CountryReportModelable.toCountryData(
    id: Long,
    countryId: Long,
    timestamp: Long
): CountryData = CountryData(
    id = id,
    cases = cases,
    todayCases = todayCases,
    critical = critical,
    recovered = recovered,
    deaths = deaths,
    todayDeaths = todayDeaths,
    countryId = countryId,
    entryDate = timestamp
)
