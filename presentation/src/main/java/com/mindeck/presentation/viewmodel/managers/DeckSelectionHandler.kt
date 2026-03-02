package com.mindeck.presentation.viewmodel.managers

import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.deck.command.CreateDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.toUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

/**
 * Handler для управления выбором и созданием колод.
 */
class DeckSelectionHandler @Inject constructor(
    getAllDecksUseCase: GetAllDecksUseCase,
    private val createDeckUseCase: CreateDeckUseCase,
) {
    private val _createDeckState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val createDeckState: StateFlow<UiState<Unit>> = _createDeckState

    private val createDeckMutex = Mutex()

    // Flow с колодами будет инициализирован в initialize()
    private var _decksState: StateFlow<UiState<List<Deck>>>? = null
    val decksState: StateFlow<UiState<List<Deck>>>
        get() = _decksState ?: throw IllegalStateException("DeckSelectionHandler not initialized. Call initialize() first.")

    // Сохраняем useCase для последующей инициализации
    private val getAllDecksUseCase = getAllDecksUseCase

    /**
     * Инициализация handler с корутин-скоупом из ViewModel.
     * MUST be called in ViewModel init block.
     */
    fun initialize(scope: CoroutineScope) {
        _decksState = getAllDecksUseCase()
            .toUiState()
            .stateIn(scope, started = SharingStarted.WhileSubscribed(5000), UiState.Loading)
    }

    /**
     * Создает новую колоду.
     *
     * @param deckName имя создаваемой колоды
     * @return ID созданной колоды или null в случае ошибки
     */
    suspend fun createDeck(deckName: String): Int? {
        if (!createDeckMutex.tryLock()) return null

        try {
            _createDeckState.value = UiState.Loading

            try {
                val deckId = createDeckUseCase(
                    deck = Deck(
                        deckName = deckName,
                    ),
                )
                _createDeckState.value = UiState.Idle
                return deckId
            } catch (e: DomainError.DatabaseError) {
                _createDeckState.value = UiState.Error("Failed to create deck")
            } catch (e: DomainError) {
                _createDeckState.value = UiState.Error("Something went wrong")
            }
        } finally {
            createDeckMutex.unlock()
        }
        return null
    }

    /**
     * Сбрасывает состояние ошибки создания колоды в Idle.
     */
    fun resetCreateDeckError() {
        if (_createDeckState.value is UiState.Error) {
            _createDeckState.value = UiState.Idle
        }
    }
}
