package com.mobilecoronatracker.model

interface CountryReportModelable {
    val country: String
    val cases: Int
    val todayCases: Int
    val deaths: Int
    val todayDeaths: Int
    val recovered: Int
    val critical: Int
    val active: Int
    var followed: Boolean
}
