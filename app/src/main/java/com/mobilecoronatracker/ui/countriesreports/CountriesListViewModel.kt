package com.mobilecoronatracker.ui.countriesreports

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilecoronatracker.data.source.CovidCountriesDataObserver
import com.mobilecoronatracker.data.source.CovidDataSource
import com.mobilecoronatracker.data.source.impl.CovidRestDataReader
import com.mobilecoronatracker.model.CountryReportModelable
import java.util.Locale

class CountriesListViewModel : ViewModel(), CountriesListViewModelable, CovidCountriesDataObserver {
    private var currentList: List<CountryReportModelable> = emptyList()
    private var currentFilterText: String = ""
    override val countryReports = MutableLiveData<List<CountryReportModelable>>()
    private var dataSource: CovidDataSource = CovidRestDataReader()

    init {
        dataSource.addCovidCountriesDataObserver(this)
    }

    override fun onCountriesData(data: List<CountryReportModelable>) {
        currentList = data
        postFilteredList()
    }

    override fun onFilterTextChanged(text: String) {
        currentFilterText = text
        postFilteredList()
    }

    private fun postFilteredList() {
        countryReports.postValue(
            currentList.filter {
                it.country
                    .toLowerCase(Locale.getDefault())
                    .contains(
                        currentFilterText.toLowerCase(Locale.getDefault())
                    )
            })
    }
}