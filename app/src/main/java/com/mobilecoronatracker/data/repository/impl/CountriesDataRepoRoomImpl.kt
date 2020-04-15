package com.mobilecoronatracker.data.repository.impl

import com.mobilecoronatracker.data.persistence.CoronaTrackerDatabase
import com.mobilecoronatracker.data.persistence.dao.CountryDao
import com.mobilecoronatracker.data.persistence.dao.CountryDataDao
import com.mobilecoronatracker.data.persistence.entity.Country
import com.mobilecoronatracker.data.repository.CountriesDataRepo
import com.mobilecoronatracker.data.repository.CovidDataRepo
import com.mobilecoronatracker.model.CountryReportModelable
import com.mobilecoronatracker.model.CountryReportTimePointModelable
import com.mobilecoronatracker.model.impl.CountryReportModel
import com.mobilecoronatracker.model.impl.CountryReportTimePointModel
import com.mobilecoronatracker.model.toCountryData
import com.mobilecoronatracker.utils.getTodayTimestamp
import com.mobilecoronatracker.utils.getYesterdayTimestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
        return getAllCountriesForDate(yesterdayTimestamp)
    }

    override fun getCountryHistory(countryName: String): Flow<List<CountryReportTimePointModelable>> {
        return countryDataDao.getAllCountryEntries(countryName).map { list ->
            list.map {
                CountryReportTimePointModel(it)
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

    override suspend fun hasTodayCountryData(): Boolean {
        val todayTimestamp = getTodayTimestamp()
        return countryDataDao.getAllCountriesCountByTimestamp(todayTimestamp) != 0
    }

    override suspend fun refreshCountriesData() {
        val countiesReportModelable = covidDataRepo.getCountriesData()
        val todayTimestamp = getTodayTimestamp()
        coronaTrackerDatabase.withTransactionWrapper {
            countiesReportModelable.forEach {
                var countryId = countryDao.insert(Country(0, it.country, it.country))
                if (countryId == -1L) {
                    countryId = countryDao.getByCountryName(it.country)?.id ?: 0L
                }
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
}
