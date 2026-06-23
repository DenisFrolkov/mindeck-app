package com.mindeck.core.ui.sheet

data class MediaSheetContent(
    val title: String,
    val description: String,
    val sources: List<MediaSource>,
    val linkLabel: String,
    val linkPlaceholder: String,
    val confirmLabel: String,
)
