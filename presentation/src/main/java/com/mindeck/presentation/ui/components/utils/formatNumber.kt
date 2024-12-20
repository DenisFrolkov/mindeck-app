package com.mindeck.presentation.ui.components.utils

fun formatNumber(number: Int, limit: Int = 999): String {
    return if (number > limit) "$limit+" else number.toString()
}