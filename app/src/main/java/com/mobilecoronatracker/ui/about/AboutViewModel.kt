package com.mobilecoronatracker.ui.about

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mobilecoronatracker.R

class AboutViewModel(@NonNull app: Application) : AndroidViewModel(app), AboutViewModelable {
    override val credits = MutableLiveData<List<AboutViewModelable.AboutSection>>()
    override val onOpenUrl = MutableLiveData<String>()

    init {
        val iconsAuthors = listOf(
            getString(R.string.credits_freepik_from_flaticon),
            getString(R.string.credits_pixelperfect_from_flaticon)
        )
        val licenseInfo = listOf(
            getString(R.string.apache_2_license_label),
            System.lineSeparator(),
            getString(R.string.apache_2_limitation_of_liablity_excerpt),
            System.lineSeparator(),
            System.lineSeparator(),
            getString(R.string.full_license_direction_label),
            System.lineSeparator(),
            getString(R.string.full_license_url)
        )
        val list = listOf<AboutViewModelable.AboutSection>(
            AboutViewModelable.AboutSection(
                getString(R.string.general_info_label),
                getString(R.string.about_text)
            ),
            AboutViewModelable.AboutSection(
                getString(R.string.license_label),
                licenseInfo.reduce { acc, s -> acc + s }
            ),
            AboutViewModelable.AboutSection(
                getString(R.string.icons_authors_label),
                iconsAuthors.reduce { acc, s -> acc + System.lineSeparator() + s }
            ),
            AboutViewModelable.AboutSection(
                getString(R.string.data_source),
                getString(R.string.credits_elite_da_myth_github)
            )
        )
        credits.postValue(list)
    }

    override fun onNavigateToGithubRepoRequested() {
        onOpenUrl.postValue(getString(R.string.repo_link))
    }

    private fun getString(stringId: Int): String {
        val context = getApplication<Application>().baseContext
        return context.getString(stringId)
    }
}