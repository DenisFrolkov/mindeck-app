package com.mindeck.presentation.ui.components.utils

import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun dimenDpResource(@DimenRes id: Int): Dp {
    val resources = LocalContext.current.resources
    return with(LocalContext.current.resources.displayMetrics) {
        resources.getDimension(id) / density
    }.dp
}
