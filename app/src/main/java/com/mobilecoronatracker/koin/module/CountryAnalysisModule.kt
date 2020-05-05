package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.ui.countryanalysis.CountryAnalysisViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryAnalysisModule = module {
    viewModel { CountryAnalysisViewModel(get(), get(), get()) }
}