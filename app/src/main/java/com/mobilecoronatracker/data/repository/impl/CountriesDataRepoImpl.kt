package com.mobilecoronatracker.data.repository.impl

import com.mobilecoronatracker.data.persistence.AppDatabase
import com.mobilecoronatracker.data.persistence.dao.CountryDao
import com.mobilecoronatracker.data.persistence.dao.CountryDataDao
import com.mobilecoronatracker.data.persistence.entity.Country
import com.mobilecoronatracker.data.persistence.entity.CountryData
import com.mobilecoronatracker.data.repository.CountriesDataRepo
import com.mobilecoronatracker.data.repository.CovidDataRepo
import com.mobilecoronatracker.model.CountryReportModelable
import com.mobilecoronatracker.model.impl.CountryReportModel
import com.mobilecoronatracker.utils.getTodayTimestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CountriesDataRepoImpl(
    private val countryDataDao: CountryDataDao,
    private val countryDao: CountryDao,
    private val covidDataRepo: CovidDataRepo,
    private val appDatabase: AppDatabase
) : CountriesDataRepo {
    override fun getAllCountriesTodayData(): Flow<List<CountryReportModelable>> {
        val todayTimestamp = getTodayTimestamp()
        return countryDataDao.getAllCountryByTimestampFlow(todayTimestamp).map { list ->
            list.map {
                CountryReportModel(it)
            }
        }
    }

    override suspend fun hasNoTodayCountryData(): Boolean {
        val todayTimestamp = getTodayTimestamp()
        return countryDataDao.getAllCountryByTimestamp(todayTimestamp).map {
            CountryReportModel(it)
        }.isEmpty()
    }

    override suspend fun refreshCountriesData() {
        val countriesData = covidDataRepo.getCountriesData()
        val todayTimestamp = getTodayTimestamp()
        appDatabase.withTransactionWrapper {
            countriesData.forEach {
                var countryId = countryDao.insert(Country(0, it.country, it.country))
                if (countryId == -1L) {
                    countryId = countryDao.getByCountryName(it.country)?.id ?: 0L
                }
                val countryData =
                    countryDataDao.getCountryByTimestamp(countryId, todayTimestamp)
                if (countryData == null) {
                    countryDataDao.insert(
                        CountryData(
                            infected = it.cases,
                            todayInfected = it.todayCases,
                            critical = it.critical,
                            recovered = it.recovered,
                            dead = it.deaths,
                            todayDead = it.todayDeaths,
                            countryId = countryId,
                            entryDate = todayTimestamp
                        )
                    )
                } else {
                    countryDataDao.update(
                        countryData.copy(
                            infected = it.cases,
                            todayInfected = it.todayCases,
                            dead = it.deaths,
                            todayDead = it.todayDeaths,
                            critical = it.critical,
                            recovered = it.recovered
                        )
                    )
                }
            }
        }
    }
}
