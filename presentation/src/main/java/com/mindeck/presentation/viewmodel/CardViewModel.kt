package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.mindeck.domain.usecases.cardUseCase.DeleteCardUseCase
import com.mindeck.domain.usecases.cardUseCase.GetCardByIdUseCase
import com.mindeck.domain.usecases.cardUseCase.UpdateCardUseCase
import javax.inject.Inject

class CardViewModel @Inject constructor(
    private val getCardByIdUseCase: GetCardByIdUseCase,
    private val updateCardUseCase: UpdateCardUseCase,
    private val deleteCardUseCase: DeleteCardUseCase
) : ViewModel() {

}