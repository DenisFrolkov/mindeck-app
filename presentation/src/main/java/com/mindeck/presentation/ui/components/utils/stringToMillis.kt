package com.mindeck.presentation.ui.components.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun stringToMillis(dateString: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date: Date = dateFormat.parse(dateString)
    return date.time
}