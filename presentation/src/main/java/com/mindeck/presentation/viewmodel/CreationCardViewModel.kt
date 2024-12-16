package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.usecases.cardUseCase.CreateCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreationCardViewModel @Inject constructor(
    private val createCardUseCase: CreateCardUseCase
) : ViewModel() {
    fun createCard(card: Card) {
        viewModelScope.launch {
            createCardUseCase.invoke(card)
        }
    }
}