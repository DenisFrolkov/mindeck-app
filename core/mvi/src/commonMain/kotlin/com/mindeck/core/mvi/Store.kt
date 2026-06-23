package com.mindeck.core.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Store<State, Intent, Effect> {
    val state: StateFlow<State>

    val effects: Flow<Effect>

    fun accept(intent: Intent)
}
