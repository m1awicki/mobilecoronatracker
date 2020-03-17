package com.mobilecoronatracker.ui.about

import androidx.lifecycle.LiveData
import com.mobilecoronatracker.model.CreditsEntryModel

interface CreditsListViewModelable {
    val credits: LiveData<List<CreditsEntryModel>>
}