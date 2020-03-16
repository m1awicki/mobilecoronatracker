package com.mobilecoronatracker.data.source

import com.mobilecoronatracker.model.GeneralReportModelable

interface CovidCumulatedDataObserver : DataObserver {
    fun onCumulatedData(data: GeneralReportModelable)
}