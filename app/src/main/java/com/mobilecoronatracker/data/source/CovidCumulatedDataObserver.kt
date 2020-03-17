package com.mobilecoronatracker.data.source

import com.mobilecoronatracker.model.GeneralReportModelable

interface CovidCumulatedDataObserver : DataErrorObserver {
    fun onCumulatedData(data: GeneralReportModelable)
}