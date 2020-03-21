package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.ui.countriesreports.CountriesListViewModel
import com.mobilecoronatracker.ui.countriesreports.CountriesListViewModelable
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countriesReportsModule = module {
    viewModel { CountriesListViewModel(get(), get()) }
}
