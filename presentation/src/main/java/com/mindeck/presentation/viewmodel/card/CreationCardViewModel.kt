package com.mindeck.presentation.viewmodel.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardType
import com.mindeck.domain.usecases.card.command.CreateCardUseCase
import com.mindeck.domain.usecases.card.command.SaveAudioUseCase
import com.mindeck.domain.usecases.card.command.SaveImageFromFileUseCase
import com.mindeck.domain.usecases.card.command.SaveImageFromUrlUseCase
import com.mindeck.domain.usecases.deck.command.CreateDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.presentation.R
import com.mindeck.presentation.state.CreateCardFormState
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.viewmodel.managers.DeckSelectionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@HiltViewModel
internal class CreationCardViewModel @Inject constructor(
    private val createCardUseCase: CreateCardUseCase,
    getAllDecksUseCase: GetAllDecksUseCase,
    createDeckUseCase: CreateDeckUseCase,
    private val saveImageFromFileUseCase: SaveImageFromFileUseCase,
    private val saveImageFromUrlUseCase: SaveImageFromUrlUseCase,
    private val saveAudioUseCase: SaveAudioUseCase,
) : ViewModel() {

    private val deckSelectionHandler =
        DeckSelectionHandler(getAllDecksUseCase, createDeckUseCase, viewModelScope)

    val decksState = deckSelectionHandler.decksState
    val createDeckState = deckSelectionHandler.createDeckState

    private val _navigationEvent = Channel<CreationCardNavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _formState = MutableStateFlow(CreateCardFormState())
    val formState: StateFlow<CreateCardFormState> = _formState.asStateFlow()

    private val _modalState = MutableStateFlow<ModalState>(ModalState.None)
    val modalState: StateFlow<ModalState> = _modalState.asStateFlow()

    private val _createCardState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val createCardState: StateFlow<UiState<Unit>> = _createCardState.asStateFlow()

    private val createCardMutex = Mutex()

    fun saveImage(uri: String) {
        viewModelScope.launch {
            val path = saveImageFromFileUseCase(uri)
            _formState.update { it.copy(cardImagePath = path) }
        }
    }

    fun saveImageFromUrl(url: String) {
        viewModelScope.launch {
            val path = saveImageFromUrlUseCase(url)
            _formState.update { it.copy(cardImagePath = path) }
        }
    }

    fun saveQuestionAudio(uri: String) {
        viewModelScope.launch {
            val path = saveAudioUseCase(uri)
            _formState.update { it.copy(cardQuestionAudioPath = path) }
        }
    }

    fun saveAnswerAudio(uri: String) {
        viewModelScope.launch {
            val path = saveAudioUseCase(uri)
            _formState.update { it.copy(cardAnswerAudioPath = path) }
        }
    }

    fun createCard(question: String, answer: String) {
        viewModelScope.launch {
            if (!createCardMutex.tryLock()) return@launch

            try {
                if (!_formState.value.isValid()) return@launch

                _createCardState.update { UiState.Loading }

                val form = _formState.value
                val selectedType = form.selectedType ?: run {
                    _createCardState.update { UiState.Error(R.string.error_card_type_required) }
                    return@launch
                }
                val selectedDeckId = form.selectedDeckId ?: run {
                    _createCardState.update { UiState.Error(R.string.error_deck_selection_required) }
                    return@launch
                }

                createCardUseCase(
                    card = Card(
                        cardName = form.title,
                        cardQuestion = question,
                        cardAnswer = answer,
                        cardType = selectedType,
                        cardTag = form.tag,
                        deckId = selectedDeckId,
                        cardImagePath = form.cardImagePath,
                        cardQuestionAudioPath = form.cardQuestionAudioPath,
                        cardAnswerAudioPath = form.cardAnswerAudioPath,
                    ),
                )
                _createCardState.update { UiState.Success(Unit) }
                clearFormFields()
                _navigationEvent.send(CreationCardNavigationEvent.ShowToast(R.string.toast_card_created_successfully))
            } catch (e: DomainError.NameAlreadyExists) {
                _createCardState.update { UiState.Error(R.string.error_card_name_already_exists) }
            } catch (e: DomainError.DatabaseError) {
                _createCardState.update { UiState.Error(R.string.error_failed_to_create_card) }
            } catch (e: DomainError) {
                _createCardState.update { UiState.Error(R.string.error_something_went_wrong) }
            } catch (e: Exception) {
                _createCardState.update { UiState.Error(R.string.error_something_went_wrong) }
            } finally {
                createCardMutex.unlock()
            }
        }
    }

    fun createDeck(deckName: String) {
        viewModelScope.launch {
            val deckId = deckSelectionHandler.createDeck(deckName)
            deckId?.let {
                _formState.update { it.copy(selectedDeckId = deckId) }
                hideModal()
            }
        }
    }

    fun updateForm(update: CreateCardFormState.() -> CreateCardFormState) {
        _formState.update { it.update() }
        clearError()
    }

    fun setDeckId(deckId: Int) {
        hideModal()
        _formState.update { it.copy(selectedDeckId = deckId) }
    }

    fun setType(type: CardType) {
        hideModal()
        _formState.update { it.copy(selectedType = type) }
    }

    fun showDeckModal() {
        _modalState.update { ModalState.DeckSelection }
        deckSelectionHandler.resetCreateDeckError()
    }

    fun showTypeModal() {
        _modalState.update { ModalState.TypeSelection }
    }

    fun hideModal() {
        _modalState.update { ModalState.None }
    }

    private fun clearFormFields() {
        _formState.update { it.copy(title = "", tag = "", cardImagePath = null) }
    }

    private fun clearError() {
        if (_createCardState.value is UiState.Error) {
            _createCardState.update { UiState.Idle }
        }
    }
}
