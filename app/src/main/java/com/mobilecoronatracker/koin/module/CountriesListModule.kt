package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.ui.countrieslist.CountriesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countriesListModule = module {
    viewModel { CountriesListViewModel(get(), get()) }
}