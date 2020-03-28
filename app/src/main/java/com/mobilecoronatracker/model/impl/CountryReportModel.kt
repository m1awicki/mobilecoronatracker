package com.mobilecoronatracker.model.impl

import com.mobilecoronatracker.data.persistence.entity.CountryDataWithCountryName
import com.mobilecoronatracker.model.CountryReportModelable
import com.mobilecoronatracker.model.pojo.CovidCountryEntry
import kotlin.math.max

data class CountryReportModel(
    override val country: String,
    override val cases: Int,
    override val todayCases: Int,
    override val deaths: Int,
    override val todayDeaths: Int,
    override val recovered: Int,
    override val critical: Int,
    override val active: Int,
    override var followed: Boolean
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
                followed = false
            )

    constructor(data: CountryDataWithCountryName) :
            this(
                data.countryName,
                data.countryData.infected,
                data.countryData.todayInfected,
                data.countryData.dead,
                data.countryData.todayDead,
                data.countryData.recovered,
                data.countryData.critical,
                max(
                    data.countryData.infected - data.countryData.recovered - data.countryData.dead,
                    0
                ),
                followed = false
            )
}
