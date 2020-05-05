package com.mobilecoronatracker.ui.countrieslist

interface CountryFollowListener {
    fun onCountryFollowed(countryName: String)
    fun onCountryUnfollowed(countryName: String)
}