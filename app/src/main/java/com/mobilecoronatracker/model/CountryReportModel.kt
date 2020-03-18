package com.mobilecoronatracker.model

import androidx.lifecycle.MutableLiveData
import com.mobilecoronatracker.model.pojo.CovidCountryEntry
import java.util.Random

data class CountryReportModel(
    override val country: String,
    override val cases: Int,
    override val todayCases: Int,
    override val deaths: Int,
    override val todayDeaths: Int,
    override val recovered: Int,
    override val critical: Int,
    override val flagged: Boolean,
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
                Random().nextDouble() > 0.5,
                Random().nextDouble() > 0.5
            )
}