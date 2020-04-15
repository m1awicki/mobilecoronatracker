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
    val casesPerMillion: Double
    val deathsPerMillion: Double
    val tests: Int
    val testsPerMillion: Double
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
    casesPerMillion = casesPerMillion,
    deathsPerMillion = deathsPerMillion,
    tests = tests,
    testsPerMillion = testsPerMillion,
    countryId = countryId,
    entryDate = timestamp
)
