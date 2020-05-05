package com.mobilecoronatracker.model

import com.mobilecoronatracker.data.persistence.entity.AccumulatedData

interface GeneralReportModelable {
    var cases: Int
    var deaths: Int
    var recovered: Int
    var critical: Int
    var tests: Int
    var todayCases: Int
    var todayDeaths: Int
    var casesPerMillion: Double
    var deathsPerMillion: Double
    var testPerMillion: Double
    var affectedCountries: Int
}

fun GeneralReportModelable.toAccumulatedData(id: Long, timestamp: Long): AccumulatedData =
    AccumulatedData(
        id,
        cases,
        deaths,
        recovered,
        critical,
        tests,
        todayCases,
        todayDeaths,
        casesPerMillion,
        deathsPerMillion,
        testPerMillion,
        affectedCountries,
        timestamp
    )
