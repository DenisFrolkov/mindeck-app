package com.mindeck.core.ui.sheet

import org.jetbrains.compose.resources.DrawableResource

/**
 * One selectable media source shown as a row inside [AddMediaBottomSheet]
 * (e.g. camera, gallery, files).
 */
data class MediaSource(
    val icon: DrawableResource,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit,
)
