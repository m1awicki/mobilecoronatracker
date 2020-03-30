package com.mobilecoronatracker.ui.chart.williamchart.extensions

// Copied from https://github.com/diogobernardino/WilliamChart

inline fun <T, R : Comparable<R>> Iterable<T>.maxValueBy(selector: (T) -> R): R? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    val maxElem = iterator.next()
    var maxValue = selector(maxElem)
    if (!iterator.hasNext()) return maxValue
    do {
        val e = iterator.next()
        val v = selector(e)
        if (maxValue < v) {
            maxValue = v
        }
    } while (iterator.hasNext())
    return maxValue
}
