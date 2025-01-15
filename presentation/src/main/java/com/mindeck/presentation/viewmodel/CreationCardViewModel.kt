package com.mindeck.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.Folder
import com.mindeck.domain.usecases.cardUseCase.CreateCardUseCase
import com.mindeck.domain.usecases.deckUseCases.GetAllDecksByFolderIdUseCase
import com.mindeck.domain.usecases.folderUseCases.GetAllFoldersUseCase
import com.mindeck.presentation.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CardState(
    var title: String,
    var question: String,
    var answer: String,
    var tag: String,
    var deckId: Int
)

data class DropdownState(
    var selectedFolder: Pair<String, Int?>,
    var selectedDeck: Pair<String, Int?>,
    var selectedType: Pair<String, Int?>
)

@HiltViewModel
class CreationCardViewModel @Inject constructor(
    private val createCardUseCase: CreateCardUseCase,
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getAllDecksByFolderIdUseCase: GetAllDecksByFolderIdUseCase
) : ViewModel() {
    private var _cardState = mutableStateOf(
        CardState("", "", "", "", -1)
    )
    val cardState: State<CardState> = _cardState

    private val _validation = MutableStateFlow<Boolean?>(null)
    val validation: StateFlow<Boolean?> = _validation.asStateFlow()

    private var _dropdownState = mutableStateOf(
        DropdownState(
            Pair("Выберите папку", null),
            Pair("Выберите колоду", null),
            Pair("Выберите тип карточки", null)
        )
    )
    val dropdownState: State<DropdownState> = _dropdownState

    private val _foldersState = MutableStateFlow<UiState<List<Folder>>>(UiState.Loading)
    val foldersState: StateFlow<UiState<List<Folder>>> = _foldersState

    private val _deckState = MutableStateFlow<UiState<List<Deck>>>(UiState.Loading)
    val deckState: StateFlow<UiState<List<Deck>>> = _deckState

    fun getAllFolders() {
        viewModelScope.launch {
            try {
                _foldersState.value = UiState.Loading
                getAllFoldersUseCase().collect { folders ->
                    _foldersState.value = UiState.Success(folders)
                }
            } catch (e: Exception) {
                _foldersState.value = UiState.Error(e)
            }
        }
    }

    fun getAllDecksByFolderId(folderId: Int) {
        viewModelScope.launch {
            try {
                getAllDecksByFolderIdUseCase(folderId = folderId).collect { decks ->
                    _deckState.value = UiState.Success(decks)
                }
            } catch (e: Exception) {
                _deckState.value = UiState.Error(e)
            }
        }
    }

    fun createCard(card: Card) {
        viewModelScope.launch {
            createCardUseCase.invoke(card)
        }
    }

    fun updateCardState(update: CardState.() -> CardState) {
        viewModelScope.launch {
            _cardState.value = _cardState.value.update()
        }
    }

    fun updateDropdownState(update: DropdownState.() -> DropdownState) {
        viewModelScope.launch {
            _dropdownState.value = _dropdownState.value.update()
        }
    }

    fun validateInput(cardState: CardState, dropdownState: DropdownState) {
        _validation.value = cardState.title.isNotBlank() &&
                cardState.question.isNotBlank() &&
                cardState.answer.isNotBlank() &&
                dropdownState.selectedDeck.second != null &&
                dropdownState.selectedType.second != null
    }

    fun resetValidation() {
        _validation.value = null
    }

    fun clear() {
        _validation.value = null
        updateCardState { copy(title = "", question = "", answer = "", tag = "", deckId = -1) }
        updateDropdownState { copy(
            selectedFolder = Pair("Выберите папку", null),
            selectedDeck = Pair("Выберите колоду", null),
            selectedType = Pair("Выберите тип карточки", null)
        ) }
    }
}