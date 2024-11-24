package com.mindeck.presentation.ui.components.utils

fun getPluralForm(number: Int): Int {
    val n = number % 100
    val n1 = number % 10
    return when {
        n in 11..19 -> 5
        n1 == 1 -> 1
        n1 in 2..4 -> 3
        else -> 5
    }
}