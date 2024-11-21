package com.mindeck.presentation.ui.components.fab

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class FabState(
    initialExpanded: Boolean = false,
    private val collapsedSize: Dp = 56.dp,
    private val expandedWidth: Dp = 196.dp,
    private val expandedHeight: Dp,
    val animationDuration: Int = 100
) {

    companion object {
        const val ITEM_HEIGHT = 50
    }

    var isExpanded by mutableStateOf(initialExpanded)
        private set

    val fabWidth: Dp
        get() = if (isExpanded) expandedWidth else collapsedSize

    val fabHeight: Dp
        get() = if (isExpanded) expandedHeight else collapsedSize

    val fabShape: Dp
        get() = if (isExpanded) 10.dp else 50.dp

    val menuAlphaValue: Float
        get() = if (isExpanded) 1F else 0F

    val fabAlphaValue: Float
        get() = if (isExpanded) 0F else 1F

    val screenAlphaValue: Float
        get() = if (isExpanded) 0.3F else 0F

    fun open() {
        isExpanded = true
    }

    fun reset() {
        isExpanded = false
    }
}