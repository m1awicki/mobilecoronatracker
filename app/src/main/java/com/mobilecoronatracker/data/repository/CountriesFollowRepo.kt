package com.mobilecoronatracker.data.repository

interface CountriesFollowRepo {
    fun addFollowedCountry(country: String)
    fun removeFollowedCountry(country: String)
    fun getFollowedCountries(): List<String>
}
