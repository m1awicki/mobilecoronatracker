package com.mobilecoronatracker.data.repository

import com.mobilecoronatracker.model.CountryReportModelable
import com.mobilecoronatracker.model.GeneralReportModelable
import com.mobilecoronatracker.ui.cumulatedreport.CumulatedReportViewModelable

interface CovidDataRepo {
    suspend fun getCountriesData(): List<CountryReportModelable>
    suspend fun getCumulatedData(): GeneralReportModelable
}
