package com.mobilecoronatracker.data.persistence.impl

import android.content.Context
import android.content.SharedPreferences
import com.mobilecoronatracker.data.persistence.CountriesFollowRepo

class SharedPreferencesCountriesFollowRepo(context: Context) : CountriesFollowRepo {
    private val sharedPrefs: SharedPreferences

    init {
        sharedPrefs = context.getSharedPreferences(SHARED_PREFS_FILE_NAME, SHARED_PREFS_MODE)
    }

    override fun addFollowedCountry(country: String) {
        val followed = sharedPrefs.getStringSet(FOLLOWED_COUNTRIES, emptySet()) ?: emptySet()
        if (followed.contains(country)) {
            return
        }
        val newSet = HashSet(followed)
        newSet.add(country)
        sharedPrefs.edit().putStringSet(FOLLOWED_COUNTRIES, newSet).apply()
    }

    override fun removeFollowedCountry(country: String) {
        val followed = sharedPrefs.getStringSet(FOLLOWED_COUNTRIES, emptySet()) ?: emptySet()
        if (followed.contains(country)) {
            val newSet = HashSet(followed)
            newSet.remove(country)
            sharedPrefs.edit().putStringSet(FOLLOWED_COUNTRIES, newSet).apply()
        }
    }

    override fun getFollowedCountries(): List<String> {
        val followed = sharedPrefs.getStringSet(FOLLOWED_COUNTRIES, emptySet()) ?: emptySet()
        return followed.toList()
    }

    companion object {
        const val FOLLOWED_COUNTRIES = "corona.tracker.prefs.followed"
        const val SHARED_PREFS_FILE_NAME = "corona.tracker.prefs"
        const val SHARED_PREFS_MODE = Context.MODE_PRIVATE
    }
}