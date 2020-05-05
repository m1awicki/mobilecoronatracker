package com.mobilecoronatracker.model.impl

import com.mobilecoronatracker.data.persistence.entity.AccumulatedData
import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.model.pojo.CovidCumulatedData

class GeneralReportModel(
    override var cases: Int = 0,
    override var deaths: Int = 0,
    override var recovered: Int = 0,
    override var critical: Int = 0,
    override var tests: Int = 0,
    override var todayCases: Int = 0,
    override var todayDeaths: Int = 0,
    override var casesPerMillion: Double = 0.0,
    override var deathsPerMillion: Double = 0.0,
    override var testPerMillion: Double = 0.0,
    override var affectedCountries: Int = 0
) : GeneralReportModelable {
    constructor(data: CovidCumulatedData) : this(
        data.cases, data.deaths, data.recovered, data.critical, data.tests, data.todayCases,
        data.todayDeaths, data.casesPerMillion, data.deathsPerMillion, data.testPerMillion,
        data.affectedCountries
    )
    constructor(data: AccumulatedData) : this(
        data.cases, data.deaths, data.recovered, data.critical, data.tests, data.todayCases,
        data.todayDeaths, data.casesPerMillion, data.deathsPerMillion, data.testPerMillion,
        data.affectedCountries
    )
}