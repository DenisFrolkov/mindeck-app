package com.mindeck.core.mvi

import kotlinx.coroutines.flow.StateFlow

interface Store<State, Intent> {
    val state: StateFlow<State>
    fun accept(intent: Intent)
}