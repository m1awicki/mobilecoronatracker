package com.mobilecoronatracker.model.pojo

data class CovidCumulatedData(
    val cases: Int = 0,
    val deaths: Int = 0,
    val recovered: Int = 0,
    val critical: Int = 0,
    val tests: Int = 0,
    val todayCases: Int = 0,
    val todayDeaths: Int = 0,
    val casesPerMillion: Double = 0.0,
    val deathsPerMillion: Double = 0.0,
    val testPerMillion: Double = 0.0,
    val affectedCountries: Int = 0,
    val updated: Long = 0
)