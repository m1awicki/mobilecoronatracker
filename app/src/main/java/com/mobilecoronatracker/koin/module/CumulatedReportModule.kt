package com.mobilecoronatracker.koin.module

import com.mobilecoronatracker.ui.accumulatedcharts.AccumulatedChartsViewModel
import com.mobilecoronatracker.ui.cumulatedreport.CumulatedReportViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cumulatedReportModule = module {
    viewModel { CumulatedReportViewModel(get(), get()) }
    viewModel { AccumulatedChartsViewModel(get()) }
}
