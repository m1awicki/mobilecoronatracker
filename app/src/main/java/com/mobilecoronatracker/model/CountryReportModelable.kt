package com.mobilecoronatracker.model

import androidx.lifecycle.LiveData

interface CountryReportModelable {
    val country: String
    val cases: Int
    val todayCases: Int
    val deaths: Int
    val todayDeaths: Int
    val recovered: Int
    val critical: Int
    val flagged: LiveData<Boolean>
    val hasMoreData: Boolean
}