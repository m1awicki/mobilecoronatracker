package com.mobilecoronatracker.model.pojo

data class CovidCountryHistory(
    val country: String = "",
    val province: String? = "",
    val timeline: Timeline = Timeline()
)
