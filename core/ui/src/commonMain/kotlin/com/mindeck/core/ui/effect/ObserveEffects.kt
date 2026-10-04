package com.mindeck.core.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow

@Composable
fun <Effect> ObserveEffects(
    effects: Flow<Effect>,
    onEffect: suspend (Effect) -> Unit,
) {
    val currentOnEffect by rememberUpdatedState(onEffect)

    LaunchedEffect(effects) {
        effects.collect { currentOnEffect(it) }
    }
}
