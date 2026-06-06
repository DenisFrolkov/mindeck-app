package com.mindeck.feature.home.home

sealed interface HomeIntent {
    data object Retry : HomeIntent
}
