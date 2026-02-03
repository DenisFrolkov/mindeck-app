package com.mindeck.presentation.ui.components.utils

import android.annotation.SuppressLint
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
@SuppressLint("NewApi")
fun dimenFloatResource(@DimenRes id: Int): Float {
    val context = LocalContext.current
    return context.resources.getFloat(id)
}

@Composable
fun dimenDpResource(@DimenRes id: Int): Dp {
    val resources = LocalContext.current.resources
    return with(LocalContext.current.resources.displayMetrics) {
        resources.getDimension(id) / density
    }.dp
}
