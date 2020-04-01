package com.mobilecoronatracker.data.repository.impl

import com.mobilecoronatracker.data.persistence.CoronaTrackerDatabase
import com.mobilecoronatracker.data.persistence.dao.AccumulatedDataDao
import com.mobilecoronatracker.data.persistence.dao.CountryDao
import com.mobilecoronatracker.data.persistence.dao.CountryDataDao
import com.mobilecoronatracker.data.persistence.entity.AccumulatedData
import com.mobilecoronatracker.data.persistence.entity.Country
import com.mobilecoronatracker.data.repository.CovidDataRepo
import com.mobilecoronatracker.data.repository.RepoInitStrategy
import com.mobilecoronatracker.model.pojo.toCountryDataList
import com.mobilecoronatracker.model.toAccumulatedData
import com.mobilecoronatracker.model.toCountryData
import com.mobilecoronatracker.utils.getTodayTimestamp

class CovidOnlyInitStrategy(
    private val covidDataRepo: CovidDataRepo,
    private val countryDataDao: CountryDataDao,
    private val countryDao: CountryDao,
    private val accumulatedDataDao: AccumulatedDataDao,
    private val coronaTrackerDatabase: CoronaTrackerDatabase
) : RepoInitStrategy {
    private suspend fun fetchNewestAccumulatedData() {
        val todayTimestamp = getTodayTimestamp()
        val generalReport = covidDataRepo.getCumulatedData()
        val storedData = accumulatedDataDao.getByTimestamp(todayTimestamp)
        if (storedData != null) {
            accumulatedDataDao.update(generalReport.toAccumulatedData(
                    storedData.id,
                    storedData.entryDate
                )
            )
        } else {
            accumulatedDataDao.insert(
                generalReport.toAccumulatedData(0, todayTimestamp)
            )
        }
    }

    private suspend fun fetchNewestCountriesData() {
        val countiesReportModelable = covidDataRepo.getCountriesData()
        val todayTimestamp = getTodayTimestamp()
        coronaTrackerDatabase.withTransactionWrapper {
            countryDao.insert(
                *countiesReportModelable.map { Country(0, it.country, it.iso2) }.toTypedArray()
            )
            val countries = countryDao.getAllCountries().map {
                it.name to it
            }.toMap()
            countiesReportModelable.forEach {
                val countryId = countries[it.country]?.id ?: 0
                val countryData =
                    countryDataDao.getCountryByTimestamp(countryId, todayTimestamp)
                if (countryData == null) {
                    countryDataDao.insert(it.toCountryData(0, countryId, todayTimestamp))
                } else {
                    countryDataDao.update(
                        it.toCountryData(
                            countryData.id,
                            countryData.countryId,
                            countryData.entryDate
                        )
                    )
                }
            }
        }
    }

    private suspend fun fetchHistoricalData() {
        val historical = covidDataRepo.getHistoricalData()
        val countriesMap = countryDao.getAllCountries().map { it.name to it }.toMap()
        val accumulatedHistory: MutableMap<Long, AccumulatedData> = mutableMapOf()
        val countriesTimelines = historical.map { historicalEntry ->
            val timeline = historicalEntry.timeline.toCountryDataList(
                historicalEntry.country,
                countriesMap
            )
            timeline.forEach {
                val entry = accumulatedHistory[it.entryDate]
                accumulatedHistory[it.entryDate] = AccumulatedData(
                    id = entry?.id ?: 0,
                    cases = entry?.cases?.plus(it.cases) ?: it.cases,
                    deaths = entry?.deaths?.plus(it.deaths) ?: it.deaths,
                    recovered = entry?.recovered?.plus(it.recovered) ?: it.recovered,
                    entryDate = it.entryDate
                )
            }
            timeline
        }
        coronaTrackerDatabase.withTransactionWrapper {
            countriesTimelines.forEach { timeline ->
                timeline.forEach { countryData ->
                    if (countryData.countryId > 0) {
                        countryDataDao.insert(countryData)
                    }
                }
            }
        }
        accumulatedDataDao.insert(*accumulatedHistory.values.toTypedArray())
    }

    override suspend fun execute() {
        fetchNewestAccumulatedData()
        fetchNewestCountriesData()
        fetchHistoricalData()
    }
}