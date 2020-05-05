package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.ui.about.AboutViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val aboutModule = module {
    viewModel { AboutViewModel(get()) }
}