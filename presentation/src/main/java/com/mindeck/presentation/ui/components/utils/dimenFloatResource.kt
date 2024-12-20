package com.mindeck.presentation.ui.components.utils

import android.annotation.SuppressLint
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp

@SuppressLint("NewApi")
@Composable
fun dimenFloatResource(@DimenRes id: Int): Float {
    val context = LocalContext.current
    return context.resources.getFloat(id)
}