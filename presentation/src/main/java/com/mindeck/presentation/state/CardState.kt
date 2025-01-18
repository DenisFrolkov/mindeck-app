package com.mindeck.presentation.state

data class CardState(
    var title: String,
    var question: String,
    var answer: String,
    var tag: String,
    var deckId: Int
)