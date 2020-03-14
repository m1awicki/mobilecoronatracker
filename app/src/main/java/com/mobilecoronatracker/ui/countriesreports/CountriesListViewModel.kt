package com.mobilecoronatracker.ui.countriesreports

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilecoronatracker.data.source.CovidCountriesDataObserver
import com.mobilecoronatracker.data.source.CovidDataSource
import com.mobilecoronatracker.data.source.impl.CovidRestDataReader
import com.mobilecoronatracker.model.CountryReportModelable

class CountriesListViewModel : ViewModel(), CountriesListViewModelable, CovidCountriesDataObserver {
    override val countryReports = MutableLiveData<List<CountryReportModelable>>()
    private var dataSource: CovidDataSource = CovidRestDataReader()

    init {
        dataSource.addCovidCountriesDataObserver(this)
    }

    override fun onCountriesData(data: List<CountryReportModelable>) {
        countryReports.postValue(data)
    }
}