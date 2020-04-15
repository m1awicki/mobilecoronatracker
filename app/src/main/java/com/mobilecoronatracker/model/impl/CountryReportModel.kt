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
    override var followed: Boolean,
    override val casesPerMillion: Double,
    override val deathsPerMillion: Double,
    override val tests: Int,
    override val testsPerMillion: Double
) : CountryReportModelable {
    constructor(data: CovidCountryEntry) :
            this(
                country = data.country,
                iso2 = data.countryInfo.iso2 ?: "",
                flagPath = data.countryInfo.flag ?: "",
                cases = data.cases,
                todayCases = data.todayCases,
                deaths = data.deaths,
                todayDeaths = data.todayDeaths,
                recovered = data.recovered,
                critical = data.critical,
                active = data.active,
                followed = false,
                casesPerMillion = data.casesPerOneMillion,
                deathsPerMillion = data.deathsPerOneMillion,
                tests = data.tests,
                testsPerMillion = data.testsPerOneMillion
            )

    constructor(data: CountryDataWithCountryInfo) :
            this(
                country = data.countryName,
                iso2 = data.iso2,
                flagPath = data.flagPath,
                cases = data.countryData.cases,
                todayCases = data.countryData.todayCases,
                deaths = data.countryData.deaths,
                todayDeaths = data.countryData.todayDeaths,
                recovered = data.countryData.recovered,
                critical = data.countryData.critical,
                active = max(
                    data.countryData.cases - data.countryData.recovered - data.countryData.deaths,
                    0
                ),
                followed = false,
                casesPerMillion = data.countryData.casesPerMillion,
                deathsPerMillion = data.countryData.deathsPerMillion,
                tests = data.countryData.tests,
                testsPerMillion = data.countryData.testsPerMillion
            )
}
