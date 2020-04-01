package com.mobilecoronatracker.model.pojo

data class CovidCountryHistory(
    val country: String = "",
    val province: String? = "",
    val timeline: Timeline = Timeline()
) {
    data class Timeline(
        val cases: LinkedHashMap<String, Int> = LinkedHashMap(),
        val deaths: LinkedHashMap<String, Int> = LinkedHashMap(),
        val recovered: LinkedHashMap<String, Int> = LinkedHashMap()
    )
}
