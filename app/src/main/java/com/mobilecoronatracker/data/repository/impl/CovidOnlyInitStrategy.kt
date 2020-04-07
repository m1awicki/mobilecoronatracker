package com.mobilecoronatracker.data.repository.impl

import com.mobilecoronatracker.data.persistence.CoronaTrackerDatabase
import com.mobilecoronatracker.data.persistence.dao.AccumulatedDataDao
import com.mobilecoronatracker.data.persistence.dao.CountryDao
import com.mobilecoronatracker.data.persistence.dao.CountryDataDao
import com.mobilecoronatracker.data.persistence.entity.Country
import com.mobilecoronatracker.data.persistence.entity.CountryData
import com.mobilecoronatracker.data.repository.CovidDataRepo
import com.mobilecoronatracker.data.repository.RepoInitStrategy
import com.mobilecoronatracker.model.pojo.CovidCountryHistory
import com.mobilecoronatracker.model.pojo.Timeline
import com.mobilecoronatracker.model.pojo.toAccumulatedDataList
import com.mobilecoronatracker.model.pojo.toCountryDataList
import com.mobilecoronatracker.model.toAccumulatedData
import com.mobilecoronatracker.model.toCountryData
import com.mobilecoronatracker.utils.getTodayTimestamp
import java.util.Locale

class CovidOnlyInitStrategy(
    private val covidDataRepo: CovidDataRepo,
    private val countryDataDao: CountryDataDao,
    private val countryDao: CountryDao,
    private val accumulatedDataDao: AccumulatedDataDao,
    private val coronaTrackerDatabase: CoronaTrackerDatabase
) : RepoInitStrategy {
    override suspend fun execute() {
        fetchNewestAccumulatedData()
        fetchNewestCountriesData()
        fetchHistoricalData()
    }

    private suspend fun fetchNewestAccumulatedData() {
        val todayTimestamp = getTodayTimestamp()
        val generalReport = covidDataRepo.getCumulatedData()
        val storedData = accumulatedDataDao.getByTimestamp(todayTimestamp)
        if (storedData != null) {
            accumulatedDataDao.update(
                generalReport.toAccumulatedData(
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
        val countryHistorical = covidDataRepo.getCountryHistoricalData()
        val countriesMap = prepareCountriesMap()

        val (normalCountries, countriesFromProvinces) =
            countryHistorical.partition {
                !countriesMap.containsKey(it.province?.toLowerCase(Locale.ROOT))
            }

        val mergedCountriesHistory = mergeCountriesHistory(normalCountries)
        val countriesTimelines: MutableList<List<CountryData>> =
            mergedCountriesHistory.map { historicalEntry ->
                val timeline = historicalEntry.timeline.toCountryDataList(
                    historicalEntry.country.toLowerCase(Locale.ROOT),
                    countriesMap
                )
                timeline
            }.toMutableList()

        countriesTimelines += countriesFromProvinces.map { historicalEntry ->
            val timeline = historicalEntry.timeline.toCountryDataList(
                historicalEntry.province?.toLowerCase(Locale.ROOT) ?: "",
                countriesMap
            )
            timeline
        }

        val accumulatedHistorical = covidDataRepo.getAccumulatedHistoricalData()
        val accumulatedDataList = accumulatedHistorical.timeline.toAccumulatedDataList()

        coronaTrackerDatabase.withTransactionWrapper {
            countriesTimelines.forEach { timeline ->
                timeline.forEach { countryData ->
                    if (countryData.countryId > 0) {
                        countryDataDao.insert(countryData)
                    }
                }
            }
            accumulatedDataDao.insert(*accumulatedDataList.toTypedArray())
        }
    }

    private suspend fun prepareCountriesMap(): Map<String, Country> {
        val countriesMap =
            countryDao.getAllCountries().map {
                it.name.toLowerCase(Locale.ROOT) to it
            }.toMap().toMutableMap()

        countryNamesMapping.forEach { entry ->
            countriesMap[entry.key.toLowerCase(Locale.ROOT)]?.let {
                countriesMap[entry.value.toLowerCase(Locale.ROOT)] = it
            }
        }

        return countriesMap
    }

    private fun mergeCountriesHistory(normalCountries: List<CovidCountryHistory>): List<CovidCountryHistory> {
        val groupedCountryHistory = normalCountries.groupBy { it.country }.values
        return groupedCountryHistory.map { countryEntryList: List<CovidCountryHistory> ->
            if (countryEntryList.size == 1) {
                return@map countryEntryList[0]
            }

            val casesMap = linkedMapOf<String, Int>()
            val deathsMap = linkedMapOf<String, Int>()
            val recoveredMap = linkedMapOf<String, Int>()

            countryEntryList.forEach { history ->
                history.timeline.cases.map { cases ->
                    casesMap[cases.key] = (casesMap[cases.key] ?: 0) + cases.value
                    deathsMap[cases.key] =
                        (deathsMap[cases.key] ?: 0) + (history.timeline.deaths[cases.key] ?: 0)
                    recoveredMap[cases.key] =
                        (recoveredMap[cases.key] ?: 0) + (history.timeline.recovered[cases.key]
                            ?: 0)
                }
            }

            countryEntryList[0].copy(
                province = null,
                timeline = Timeline(casesMap, deathsMap, recoveredMap)
            )
        }
    }

    companion object {
        val countryNamesMapping = mapOf(
            "Réunion" to "reunion",
            "Côte d'Ivoire" to "Cote d'Ivoire",
            "Palestinian Territory, Occupied" to "West Bank and Gaza",
            "Macao" to "Macau",
            "Saint Martin" to "st martin",
            "Myanmar" to "Burma",
            "Curaçao" to "curacao",
            "Lao People's Democratic Republic" to "Lao People\"s Democratic Republic",
            "Holy See (Vatican City State)" to "Holy See",
            "St. Barth" to "saint barthelemy",
            "Caribbean Netherlands" to "bonaire, sint eustatius and saba",
            "Falkland Islands (Malvinas)" to "falkland islands (islas malvinas)",
            "Saint Pierre Miquelon" to "saint pierre and miquelon"
        )
    }
}