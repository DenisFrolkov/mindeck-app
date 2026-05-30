package com.mindeck.presentation.ui.navigation

sealed interface Child  {
    data object Main: Child
}