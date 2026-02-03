package com.mindeck.presentation.state

data class DropdownState(
    var selectedFolder: Pair<String, Int?>,
    var selectedDeck: Pair<String, Int?>,
    var selectedType: Pair<String, Int?>,
)
