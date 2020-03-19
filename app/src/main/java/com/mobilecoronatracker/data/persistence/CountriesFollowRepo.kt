package com.mobilecoronatracker.data.persistence

interface CountriesFollowRepo {
    fun addFollowedCountry(country: String)
    fun removeFollowedCountry(country: String)
    fun getFollowedCountries(): List<String>
}