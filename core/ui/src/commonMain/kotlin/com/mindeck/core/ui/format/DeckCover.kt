package com.mindeck.core.ui.format

fun deckCoverInitial(title: String): String = title.firstOrNull()?.uppercaseChar()?.toString() ?: ""
