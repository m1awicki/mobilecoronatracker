package com.mobilecoronatracker.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface AboutViewModelable {
    val credits: LiveData<List<AboutSection>>
    val onOpenUrl: MutableLiveData<String>

    fun onNavigateToGithubRepoRequested()

    data class AboutSection(val title: String, val description: String)
}