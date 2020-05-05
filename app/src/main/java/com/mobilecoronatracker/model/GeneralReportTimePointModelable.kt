package com.mobilecoronatracker.model

interface GeneralReportTimePointModelable {
    var cases: Int
    var deaths: Int
    var recovered: Int
    var timestamp: Long
}