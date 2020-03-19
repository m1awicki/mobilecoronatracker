package com.mobilecoronatracker.model.pojo

data class CovidCountryEntry(
    val country: String = "",
    val cases: Int = 0,
    val todayCases: Int = 0,
    val deaths: Int = 0,
    val todayDeaths: Int = 0,
    val recovered: Int = 0,
    val critical: Int = 0,
    val casesPerOneMillion: Int = 0,
    val active: Int = 0
)