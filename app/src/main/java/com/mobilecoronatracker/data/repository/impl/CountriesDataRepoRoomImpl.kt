package com.mobilecoronatracker.data.repository.impl

import android.util.Log
import com.mobilecoronatracker.data.persistence.CoronaTrackerDatabase
import com.mobilecoronatracker.data.persistence.dao.CountryDao
import com.mobilecoronatracker.data.persistence.dao.CountryDataDao
import com.mobilecoronatracker.data.persistence.entity.Country
import com.mobilecoronatracker.data.persistence.entity.CountryData
import com.mobilecoronatracker.data.repository.CountriesDataRepo
import com.mobilecoronatracker.data.repository.CovidDataRepo
import com.mobilecoronatracker.data.repository.RepoResult
import com.mobilecoronatracker.model.CountryReportModelable
import com.mobilecoronatracker.model.CountryReportTimePointModelable
import com.mobilecoronatracker.model.impl.CountryReportModel
import com.mobilecoronatracker.model.impl.CountryReportTimePointModel
import com.mobilecoronatracker.model.pojo.CovidCountryHistory
import com.mobilecoronatracker.model.pojo.Timeline
import com.mobilecoronatracker.model.pojo.toCountryDataList
import com.mobilecoronatracker.model.toCountryData
import com.mobilecoronatracker.utils.getTodayTimestamp
import com.mobilecoronatracker.utils.getYesterdayTimestamp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.Locale

@ExperimentalCoroutinesApi
class CountriesDataRepoRoomImpl(
    private val countryDataDao: CountryDataDao,
    private val countryDao: CountryDao,
    private val covidDataRepo: CovidDataRepo,
    private val coronaTrackerDatabase: CoronaTrackerDatabase
) : CountriesDataRepo {
    override fun getAllCountriesTodayData(): Flow<List<CountryReportModelable>> {
        val todayTimestamp = getTodayTimestamp()
        return getAllCountriesForDate(todayTimestamp)
    }

    override fun getAllCountriesYesterdayData(): Flow<List<CountryReportModelable>> {
        val yesterdayTimestamp = getYesterdayTimestamp()
        return getAllCountriesForDate(yesterdayTimestamp).distinctUntilChanged()
    }

    override fun getCountryHistory(countryName: String): Flow<List<CountryReportTimePointModelable>> {
        return countryDataDao.getAllCountryEntries(countryName).map { list ->
            list.map {
                CountryReportTimePointModel(it)
            }
        }
    }

    override suspend fun hasTodayCountryData(): Boolean {
        val todayTimestamp = getTodayTimestamp()
        return countryDataDao.getAllCountriesCountByTimestamp(todayTimestamp) != 0
    }

    override suspend fun fetchTodayCountriesData(): RepoResult<Unit> =
        getCountriesData(covidDataRepo.getCountriesData(), getTodayTimestamp())

    override suspend fun fetchYesterdayCountriesData(): RepoResult<Unit> =
        getCountriesData(covidDataRepo.getYesterdayCountriesData(), getYesterdayTimestamp())

    override suspend fun fetchHistoricalCountriesData() {
        val countriesMap = prepareCountriesMap()
        if (countriesMap.isEmpty()) {
            Log.d(TAG, "No countries in db, fetch today data first.")
            return
        }

        val countryHistorical = covidDataRepo.getCountryHistoricalData()
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

        coronaTrackerDatabase.withTransactionWrapper {
            countriesTimelines.forEach { timeline ->
                timeline.forEach { countryData ->
                    if (countryData.countryId > 0) {
                        countryDataDao.insert(countryData)
                    }
                }
            }
        }
    }

    private suspend fun getCountriesData(
        repoResult: RepoResult<List<CountryReportModelable>>,
        timestamp: Long
    ): RepoResult<Unit> =
        when (repoResult) {
            is RepoResult.FailureResult -> RepoResult.FailureResult(repoResult.throwable)
            is RepoResult.SuccessResult -> {
                saveCountriesData(repoResult.data, timestamp)
                RepoResult.SuccessResult(Unit)
            }
        }

    private suspend fun saveCountriesData(
        countriesReports: List<CountryReportModelable>,
        todayTimestamp: Long
    ) {
        coronaTrackerDatabase.withTransactionWrapper {
            countryDao.insert(
                *countriesReports.map { Country(0, it.country, it.iso2, it.flagPath) }
                    .toTypedArray()
            )
            val countries = countryDao.getAllCountries().map {
                it.name to it
            }.toMap()
            countriesReports.forEach {
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

    private fun getAllCountriesForDate(timestamp: Long): Flow<List<CountryReportModelable>> {
        return countryDataDao.getAllCountriesByTimestampFlow(timestamp).map { list ->
            list.map {
                CountryReportModel(it)
            }
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

        val TAG = CountriesDataRepoRoomImpl::class.java.simpleName
    }
}
