package com.mindeck.core.mvi

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Intent, Effect>(
    initialState: State,
) : Store<State, Intent, Effect> {
    val handler =
        CoroutineExceptionHandler { _, exception ->
            println("[BaseViewModel] Unhandled exception in viewModelScope: ${exception.stackTraceToString()}")
        }
    protected val viewModelScope: CoroutineScope = CoroutineScope(handler + SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<State> = _state.asStateFlow()

    private val _effects = Channel<Effect>(Channel.BUFFERED)
    override val effects: Flow<Effect> = _effects.receiveAsFlow()

    protected val currentState: State
        get() = _state.value

    protected fun updateState(reducer: (State) -> State) {
        _state.update(reducer)
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch { _effects.send(effect) }
    }

    open fun clear() {
        viewModelScope.cancel()
    }
}
