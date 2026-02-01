package com.mindeck.presentation.ui.components.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatNumber(number: Int, limit: Int = 999): String {
    return if (number > limit) "$limit+" else number.toString()
}

fun stringToMillis(dateString: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date: Date = dateFormat.parse(dateString)
    return date.time
}
