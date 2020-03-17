package com.mobilecoronatracker.ui.utils

import android.widget.TextView
import androidx.core.text.util.LinkifyCompat
import java.util.regex.Pattern

fun addLinksNoAppend(view: TextView, pattern: Pattern, scheme: String) {
    LinkifyCompat.addLinks(view, pattern, scheme, {_, _, _ -> true}, {_, _ -> ""})
}