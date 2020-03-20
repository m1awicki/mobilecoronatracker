package com.mobilecoronatracker.ui.countriesreports

interface CountryFollowListener {
    fun onCountryFollowed(countryName: String)
    fun onCountryUnfollowed(countryName: String)
}