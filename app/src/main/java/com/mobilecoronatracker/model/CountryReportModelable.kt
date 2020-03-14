package com.mobilecoronatracker.model

interface CountryReportModelable {
    var country: String
    var cases: Int
    var todayCases: Int
    var deaths: Int
    var todayDeaths: Int
    var recovered: Int
    var critical: Int
}