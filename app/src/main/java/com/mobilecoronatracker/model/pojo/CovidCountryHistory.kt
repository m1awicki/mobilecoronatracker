package com.mobilecoronatracker.model.pojo

data class CovidCountryHistory(
    val country: String = "",
    val province: String? = "",
    val timeline: Timeline = Timeline()
) {
    data class Timeline(
        val cases: Map<String, Int> = emptyMap(),
        val deaths: Map<String, Int> = emptyMap()
    )
}
