package com.mobilecoronatracker.ui.countryanalysis

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilecoronatracker.R
import com.mobilecoronatracker.data.persistence.dao.CountryDao
import com.mobilecoronatracker.data.repository.CountriesDataRepo
import com.mobilecoronatracker.model.CountryReportTimePointModelable
import com.mobilecoronatracker.utils.asSimpleDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CountryAnalysisViewModel(
    private val context: Context,
    private val countriesDataRepo: CountriesDataRepo,
    private val countriesDao: CountryDao
) :
    ViewModel(),
    CountryAnalysisViewModelable
{
    override val countryHistoryData = MutableLiveData<CountryAnalysisViewModelable.HistoryChartData>()
    override val countryActiveCasesData = MutableLiveData<CountryAnalysisViewModelable.SingleLineChartData>()
    override val dailyIncreaseData = MutableLiveData<CountryAnalysisViewModelable.SingleLineChartData>()
    override val dailyIncreaseAsPercentOfAllData = MutableLiveData<CountryAnalysisViewModelable.SingleLineChartData>()
    override val casesPerMillionData = MutableLiveData<CountryAnalysisViewModelable.CasesPerMillionData>()
    override val testedToIdentifiedData = MutableLiveData<List<Float>>()
    override val tested = MutableLiveData<Int>()
    override val infected = MutableLiveData<Int>()
    override val countryFlagPath = MutableLiveData<String>()
    override val countryName = MutableLiveData<String>()

    override fun setCountry(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadData(name)
        }
        viewModelScope.launch(Dispatchers.IO) {
            loadCountryInfo(name)
        }
    }

    private suspend fun loadData(country: String) {
        countriesDataRepo.getCountryHistory(country).collect {
            onCountryHistory(it)
        }
    }

    private suspend fun loadCountryInfo(countryName: String) {
        val country = countriesDao.getByCountryName(countryName)
        country?.let {
            countryFlagPath.postValue(it.countryFlagPath)
            this.countryName.postValue(it.name)
        }
    }

    private fun updateTestedAndIdentified(today: CountryReportTimePointModelable) {
        tested.postValue(today.tests)
        infected.postValue(today.cases)
        testedToIdentifiedData.postValue(listOf(
            today.cases.toFloat(),
            today.tests.toFloat()
        ))
    }

    private fun updateDataPerMillionCitizens(today: CountryReportTimePointModelable) {
        casesPerMillionData.postValue(
            CountryAnalysisViewModelable.CasesPerMillionData(
                listOf(
                    today.casesPerMillion.toFloat(),
                    today.deathsPerMillion.toFloat(),
                    today.testsPerMillion.toFloat()
                ),
                listOf(
                    context.resources.getString(R.string.cases_label_text),
                    context.resources.getString(R.string.deaths_label_text),
                    context.resources.getString(R.string.tested)
                )
            ))
    }

    private fun updateDailyCasesIncreases(casesTimeLine: List<Float>, casesLabels: List<String>) {
        val dailyIncrease = mutableListOf<Float>()
        casesTimeLine.forEachIndexed { index, fl ->
            if (index == 0) {
                dailyIncrease.add(fl)
            } else {
                dailyIncrease.add(
                    maxOf(fl - casesTimeLine[index - 1], 0f)
                )
            }
        }
        dailyIncreaseData.postValue(CountryAnalysisViewModelable.SingleLineChartData(
            dailyIncrease,
            casesLabels
        ))
    }

    private fun updateDailyIncreasesAsPercentOfAll(casesTimeLine: List<Float>, casesLabels: List<String>) {
        val dailyIncreaseAsPercent = mutableListOf<Float>()
        casesTimeLine.forEachIndexed { index, fl ->
            if (index == 0) {
                dailyIncreaseAsPercent.add(fl/fl * 100)
            } else {
                dailyIncreaseAsPercent.add(
                    maxOf((fl - casesTimeLine[index - 1])/fl * 100, 0f)
                )
            }
        }
        dailyIncreaseAsPercentOfAllData.postValue(CountryAnalysisViewModelable.SingleLineChartData(
            dailyIncreaseAsPercent,
            casesLabels
        ))
    }

    private fun onCountryHistory(history: List<CountryReportTimePointModelable>) {
        updateTestedAndIdentified(history.last())
        updateDataPerMillionCitizens(history.last())

        val cases = mutableListOf<Float>()
        val deaths = mutableListOf<Float>()
        val recovered = mutableListOf<Float>()
        val active = mutableListOf<Float>()
        val labels = mutableListOf<String>()
        history.forEach { timePoint ->
            if (timePoint.cases > 0) {
                cases.add(timePoint.cases.toFloat())
                deaths.add(timePoint.deaths.toFloat())
                recovered.add(timePoint.recovered.toFloat())
                active.add((timePoint.cases - timePoint.deaths - timePoint.recovered).toFloat())
                labels.add(timePoint.timestamp.asSimpleDate())
            }
        }
        countryHistoryData.postValue(CountryAnalysisViewModelable.HistoryChartData(
            listOf(
                cases,
                recovered,
                deaths
            ),
            labels
        ))
        countryActiveCasesData.postValue(CountryAnalysisViewModelable.SingleLineChartData(
            active,
            labels
        ))

        updateDailyCasesIncreases(cases, labels)
        updateDailyIncreasesAsPercentOfAll(cases, labels)
    }
}