package com.mindeck.core.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<State, Intent>(
    initialState: State,
) : Store<State, Intent> {
    protected val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<State> = _state.asStateFlow()

    protected val currentState: State
        get() = _state.value

    protected fun updateState(reducer: (State) -> State) {
        _state.update(reducer)
    }

    open fun clear() {
        viewModelScope.cancel()
    }
}
