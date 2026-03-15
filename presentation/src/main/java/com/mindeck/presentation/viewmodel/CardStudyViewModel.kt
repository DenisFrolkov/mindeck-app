package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.ReviewButton
import com.mindeck.domain.usecases.card.command.UpdateCardReviewUseCase
import com.mindeck.domain.usecases.card.query.GetCardByIdUseCase
import com.mindeck.domain.usecases.card.query.GetCardsRepetitionUseCase
import com.mindeck.presentation.R
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
internal class CardStudyViewModel @Inject constructor(
    private val getCardsRepetitionUseCase: GetCardsRepetitionUseCase,
    private val getCardByIdUseCase: GetCardByIdUseCase,
    private val updateCardReviewUseCase: UpdateCardReviewUseCase,
) : ViewModel() {

    private val _modalState = MutableStateFlow<ModalState>(ModalState.None)
    val modalState: StateFlow<ModalState> = _modalState.asStateFlow()

    private val _cardsState = MutableStateFlow<UiState<List<Card>>>(UiState.Idle)
    val cardsState: StateFlow<UiState<List<Card>>> = _cardsState.asStateFlow()

    // Кешированные метки кнопок оценки для текущей карточки.
    // Обновляются при смене карточки, а не при каждой рекомпозиции.
    private val _reviewLabels = MutableStateFlow<Map<ReviewButton, String>>(emptyMap())
    val reviewLabels: StateFlow<Map<ReviewButton, String>> = _reviewLabels.asStateFlow()

    // Внутренняя очередь сессии. Берётся снимок карточек один раз при загрузке.
    // Дальнейшее управление очередью происходит в памяти, а не через реактивный поток,
    // чтобы изменения в БД не сбрасывали текущую сессию.
    private val sessionQueue = ArrayDeque<Card>()
    private val reviewMutex = Mutex()

    // Кешируем список кнопок, чтобы не обращаться к entries (reflection) при каждой смене карточки.
    private val reviewButtonEntries = ReviewButton.entries

    // Загружает сессию повторения из алгоритма SM-2 (LEARNING → REVIEW → NEW до лимита)
    fun loadCardRepetition() {
        viewModelScope.launch {
            _cardsState.update { UiState.Loading }
            try {
                val cards = getCardsRepetitionUseCase().firstOrNull()
                if (cards == null) {
                    _cardsState.update { UiState.Error(R.string.error_get_card_for_study) }
                    return@launch
                }
                sessionQueue.clear()
                sessionQueue.addAll(cards)
                _cardsState.update { UiState.Success(sessionQueue.toList()) }
                updateReviewLabels(sessionQueue.firstOrNull())
            } catch (e: DomainError.DatabaseError) {
                _cardsState.update { UiState.Error(R.string.error_get_card_for_study) }
            } catch (e: Exception) {
                _cardsState.update { UiState.Error(R.string.error_something_went_wrong) }
            }
        }
    }

    // Загружает одну конкретную карточку для изучения (например, из экрана карточки)
    fun loadCardById(cardId: Int) {
        viewModelScope.launch {
            _cardsState.update { UiState.Loading }
            try {
                val card = getCardByIdUseCase(cardId = cardId).firstOrNull()
                sessionQueue.clear()
                if (card != null) {
                    sessionQueue.add(card)
                    _cardsState.update { UiState.Success(sessionQueue.toList()) }
                    updateReviewLabels(card)
                } else {
                    _cardsState.update { UiState.Error(R.string.error_failed_to_load_card) }
                }
            } catch (e: DomainError.DatabaseError) {
                _cardsState.update { UiState.Error(R.string.error_failed_to_load_card) }
            } catch (e: Exception) {
                _cardsState.update { UiState.Error(R.string.error_something_went_wrong) }
            }
        }
    }

    // Обрабатывает оценку карточки пользователем.
    // Карточка остаётся в сессии только пока она в фазе обучения (LEARNING / LAPSE).
    // Как только переходит в REVIEW — убирается из сессии до следующего дня.
    // Проверяем состояние ПОСЛЕ нажатия, а не саму кнопку — это корректнее:
    // HARD на REVIEW → остаётся REVIEW → уходит из сессии
    // AGAIN на REVIEW → переходит в LAPSE → остаётся в сессии
    fun reviewCard(card: Card, button: ReviewButton) {
        viewModelScope.launch {
            reviewMutex.withLock {
                try {
                    // use case возвращает актуальное состояние карточки после применения алгоритма
                    val updatedCard = updateCardReviewUseCase(card, button)

                    sessionQueue.removeFirstOrNull()

                    if (updatedCard.cardState == CardState.LEARNING || updatedCard.cardState == CardState.LAPSE) {
                        sessionQueue.addLast(updatedCard)
                    }

                    _cardsState.update { UiState.Success(sessionQueue.toList()) }
                    updateReviewLabels(sessionQueue.firstOrNull())
                } catch (e: Exception) {
                    _cardsState.update { UiState.Error(R.string.error_something_went_wrong) }
                }
            }
        }
    }

    private fun updateReviewLabels(card: Card?) {
        if (card == null) {
            _reviewLabels.update { emptyMap() }
            return
        }
        _reviewLabels.update {
            reviewButtonEntries.associateWith { button ->
                formatInterval(updateCardReviewUseCase.previewNextInterval(card, button))
            }
        }
    }

    private fun formatInterval(millis: Long): String = when {
        millis < TimeUnit.MINUTES.toMillis(1) -> "${TimeUnit.MILLISECONDS.toSeconds(millis)} сек"
        millis < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(millis)} мин"
        // Часы не показываем: REVIEW карточки снаплены к UTC-дню, поэтому
        // "9 ч" означает "завтра" — честнее показать "1 д"
        else -> "${ceil(millis.toDouble() / TimeUnit.DAYS.toMillis(1)).toInt()} д"
    }

    fun showDropdownMenu() {
        _modalState.update { ModalState.DropdownMenu }
    }

    fun hideModal() {
        _modalState.update { ModalState.None }
    }

    override fun onCleared() {
        super.onCleared()
        sessionQueue.clear()
    }
}
