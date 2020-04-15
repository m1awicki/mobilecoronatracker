package com.mobilecoronatracker.model.impl

import com.mobilecoronatracker.data.persistence.entity.CountryData
import com.mobilecoronatracker.model.CountryReportTimePointModelable

class CountryReportTimePointModel(
    override var cases: Int,
    override var deaths: Int,
    override var recovered: Int,
    override var active: Int,
    override var casesPerMillion: Double,
    override var deathsPerMillion: Double,
    override var tests: Int,
    override var testsPerMillion: Double,
    override var timestamp: Long
) : CountryReportTimePointModelable {
    constructor(data: CountryData) : this(
        data.cases,
        data.deaths,
        data.recovered,
        data.cases - data.recovered - data.deaths,
        data.casesPerMillion,
        data.deathsPerMillion,
        data.tests,
        data.testsPerMillion,
        data.entryDate
    )
}