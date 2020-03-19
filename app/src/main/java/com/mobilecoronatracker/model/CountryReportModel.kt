package com.mobilecoronatracker.model

import com.mobilecoronatracker.model.pojo.CovidCountryEntry

data class CountryReportModel(
    override val country: String,
    override val cases: Int,
    override val todayCases: Int,
    override val deaths: Int,
    override val todayDeaths: Int,
    override val recovered: Int,
    override val critical: Int,
    override val active: Int,
    override var followed: Boolean,
    override val hasMoreData: Boolean
) : CountryReportModelable {
    constructor(data: CovidCountryEntry) :
            this(
                data.country,
                data.cases,
                data.todayCases,
                data.deaths,
                data.todayDeaths,
                data.recovered,
                data.critical,
                data.active,
                followed = false,
                hasMoreData = false
            )
}
