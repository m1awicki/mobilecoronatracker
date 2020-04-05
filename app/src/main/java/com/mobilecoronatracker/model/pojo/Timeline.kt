package com.mobilecoronatracker.model.pojo

data class Timeline(
    val cases: LinkedHashMap<String, Int> = LinkedHashMap(),
    val deaths: LinkedHashMap<String, Int> = LinkedHashMap(),
    val recovered: LinkedHashMap<String, Int> = LinkedHashMap()
)
