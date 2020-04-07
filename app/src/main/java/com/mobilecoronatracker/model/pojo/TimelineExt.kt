package com.mobilecoronatracker.model.pojo

import com.mobilecoronatracker.data.persistence.entity.AccumulatedData
import com.mobilecoronatracker.data.persistence.entity.Country
import com.mobilecoronatracker.data.persistence.entity.CountryData
import com.mobilecoronatracker.data.util.impl.DateIteratorImpl
import com.mobilecoronatracker.utils.parseDate

fun Timeline.toCountryDataList(
    country: String,
    countriesMap: Map<String, Country>
): List<CountryData> {
    return cases.map {
        val date = parseDate(it.key)
        val dateIterator = DateIteratorImpl(it.key, '/')
        val prevDay = dateIterator.prevDate()
        CountryData(
            cases = it.value,
            deaths = deaths[it.key] ?: 0,
            recovered = recovered[it.key] ?: 0,
            todayCases = it.value.minus(cases[prevDay] ?: 0),
            todayDeaths = deaths[it.key]?.minus(deaths[prevDay] ?: 0) ?: 0,
            countryId = countriesMap[country]?.id ?: 0,
            entryDate = date.timeInMillis
        )
    }
}

fun Timeline.toAccumulatedDataList(): List<AccumulatedData> {
    return cases.map {
        val date = parseDate(it.key)
        AccumulatedData(
            cases = it.value,
            deaths = deaths[it.key] ?: 0,
            recovered = recovered[it.key] ?: 0,
            entryDate = date.timeInMillis
        )
    }
}