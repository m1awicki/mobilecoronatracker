package com.mobilecoronatracker.ui.cumulatedreport

import androidx.lifecycle.LiveData

interface CumulatedReportViewModelable {
    val cases: LiveData<String>
    val deaths: LiveData<String>
    val recovered: LiveData<String>
}
