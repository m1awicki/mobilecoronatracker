package com.mobilecoronatracker.model

interface CountryReportTimePointModelable {
    var cases: Int
    var deaths: Int
    var recovered: Int
    var active: Int
    var casesPerMillion: Double
    var deathsPerMillion: Double
    var tests: Int
    var testsPerMillion: Double
    var timestamp: Long
}