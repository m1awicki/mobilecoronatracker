package com.mobilecoronatracker.data.persistence

interface CountriesReportsSettings {
    fun addFollowedCountry(country: String)
    fun removeFollowedCountry(country: String)
    fun getFollowedCountries(): List<String>?
}