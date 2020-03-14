package com.mobilecoronatracker.data.source

import com.mobilecoronatracker.model.GeneralReportModelable

interface CovidCumulatedDataObserver {
    fun onCumulatedData(data: GeneralReportModelable)
}