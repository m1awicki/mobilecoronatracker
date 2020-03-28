package com.mobilecoronatracker.model.impl

import com.mobilecoronatracker.data.persistence.entity.AccumulatedData
import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.model.pojo.CovidCumulatedData

class GeneralReportModel(
    override var cases: Int,
    override var deaths: Int,
    override var recovered: Int
) : GeneralReportModelable {
    constructor(data: CovidCumulatedData) : this(data.cases, data.deaths, data.recovered)
    constructor(data: AccumulatedData) : this(data.infected, data.dead, data.recovered)
}