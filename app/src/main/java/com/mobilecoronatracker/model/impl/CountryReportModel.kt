package com.mobilecoronatracker.model.impl

import com.mobilecoronatracker.data.persistence.entity.CountryDataWithCountryInfo
import com.mobilecoronatracker.model.CountryReportModelable
import com.mobilecoronatracker.model.pojo.CovidCountryEntry
import kotlin.math.max

data class CountryReportModel(
    override val country: String,
    override val iso2: String,
    override val flagPath: String,
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
                data.countryInfo.iso2 ?: "",
                data.countryInfo.flag ?: "",
                data.cases,
                data.todayCases,
                data.deaths,
                data.todayDeaths,
                data.recovered,
                data.critical,
                data.active,
                followed = false
            )

    constructor(data: CountryDataWithCountryInfo) :
            this(
                data.countryName,
                data.iso2,
                data.flagPath,
                data.countryData.cases,
                data.countryData.todayCases,
                data.countryData.deaths,
                data.countryData.todayDeaths,
                data.countryData.recovered,
                data.countryData.critical,
                max(
                    data.countryData.cases - data.countryData.recovered - data.countryData.deaths,
                    0
                ),
                followed = false
            )
}
