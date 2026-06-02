package com.mindeck.app.navigation

sealed interface Child  {
    data object Main: Child
    data object Second: Child
}