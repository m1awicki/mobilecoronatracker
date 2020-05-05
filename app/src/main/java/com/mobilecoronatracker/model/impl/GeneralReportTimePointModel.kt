package com.mobilecoronatracker.model.impl

import com.mobilecoronatracker.data.persistence.entity.AccumulatedData
import com.mobilecoronatracker.model.GeneralReportTimePointModelable
import com.mobilecoronatracker.model.pojo.CovidCumulatedData

class GeneralReportTimePointModel(
    override var cases: Int,
    override var deaths: Int,
    override var recovered: Int,
    override var timestamp: Long
) : GeneralReportTimePointModelable {
    constructor(data: CovidCumulatedData) : this(data.cases, data.deaths, data.recovered, data.updated)
    constructor(data: AccumulatedData) : this(data.cases, data.deaths, data.recovered, data.entryDate)
}