package com.mobilecoronatracker.model

import com.mobilecoronatracker.data.persistence.entity.AccumulatedData

interface GeneralReportModelable {
    var cases: Int
    var deaths: Int
    var recovered: Int
}

fun GeneralReportModelable.toAccumulatedData(id: Long, timestamp: Long): AccumulatedData =
    AccumulatedData(id, cases, deaths, recovered, timestamp)
