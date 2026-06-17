package com.mindeck.core.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun <Effect> ObserveEffects(
    effects: Flow<Effect>,
    onEffect: suspend (Effect) -> Unit,
) {
    LaunchedEffect(effects) {
        effects.collect { onEffect(it) }
    }
}
