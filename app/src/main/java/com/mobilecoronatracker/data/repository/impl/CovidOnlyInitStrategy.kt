package com.mobilecoronatracker.data.repository.impl

import com.mobilecoronatracker.data.repository.AccumulatedDataRepo
import com.mobilecoronatracker.data.repository.CountriesDataRepo
import com.mobilecoronatracker.data.repository.RepoInitStrategy

class CovidOnlyInitStrategy(
    private val countriesDataRepo: CountriesDataRepo,
    private val accumulatedDataRepo: AccumulatedDataRepo
) : RepoInitStrategy {
    override suspend fun execute() {
        fetchNewestAccumulatedData()
        fetchNewestCountriesData()
        fetchHistoricalData()
    }

    private suspend fun fetchNewestAccumulatedData() {
        accumulatedDataRepo.fetchTodayAccumulatedData()
    }

    private suspend fun fetchNewestCountriesData() {
        countriesDataRepo.fetchTodayCountriesData()
    }

    private suspend fun fetchHistoricalData() {
        countriesDataRepo.fetchHistoricalCountriesData()
        accumulatedDataRepo.fetchHistoricalAccumulatedData()
    }
}