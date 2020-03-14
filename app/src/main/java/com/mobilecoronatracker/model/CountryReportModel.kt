package com.mobilecoronatracker.model

import com.mobilecoronatracker.model.pojo.CovidCountryEntry

class CountryReportModel(
    override var country: String,
    override var cases: Int,
    override var todayCases: Int,
    override var deaths: Int,
    override var todayDeaths: Int,
    override var recovered: Int,
    override var critical: Int
) : CountryReportModelable {
    constructor(data: CovidCountryEntry) :
            this(data.country, data.cases, data.todayCases, data.deaths, data.todayDeaths,
                data.recovered, data.critical)
}