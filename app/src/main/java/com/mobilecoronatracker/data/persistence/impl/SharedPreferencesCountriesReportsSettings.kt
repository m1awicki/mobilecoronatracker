package com.mobilecoronatracker.data.persistence.impl

import android.content.Context
import android.content.SharedPreferences
import com.mobilecoronatracker.data.persistence.CountriesReportsSettings
import kotlin.collections.HashSet

class SharedPreferencesCountriesReportsSettings(context: Context) : CountriesReportsSettings {
    private val sharedPrefs: SharedPreferences

    init {
        sharedPrefs = context.getSharedPreferences(getSharedPrefsName(), getSharedPrefsMode())
    }
    override fun addFollowedCountry(country: String) {
        val followed = sharedPrefs.getStringSet(FOLLOWED_COUNTRIES, null)
        followed?.contains(country)?.let {
            if (it) return
        }
        val newSet = HashSet(followed)
        newSet.add(country)
        sharedPrefs.edit().putStringSet(FOLLOWED_COUNTRIES, newSet)
    }

    override fun removeFollowedCountry(country: String) {
        val followed = sharedPrefs.getStringSet(FOLLOWED_COUNTRIES, null)
        followed?.contains(country)?.let {
            if (it) {
                val newSet = HashSet(followed)
                newSet.remove(country)
                sharedPrefs.edit().putStringSet(FOLLOWED_COUNTRIES, newSet)
            }
        }

    }

    override fun getFollowedCountries(): List<String>? {
        val followed = sharedPrefs.getStringSet(FOLLOWED_COUNTRIES, null)
        return followed?.toList()
    }

    companion object {
        val FOLLOWED_COUNTRIES = "corona.tracker.prefs.followed"
        val SHARED_PREFS_FILE_NAME = "corona.tracker.prefs"
        val SHARED_PREFS_MODE = Context.MODE_PRIVATE

        fun getSharedPrefsName(): String = SHARED_PREFS_FILE_NAME
        fun getSharedPrefsMode(): Int = SHARED_PREFS_MODE
    }
}