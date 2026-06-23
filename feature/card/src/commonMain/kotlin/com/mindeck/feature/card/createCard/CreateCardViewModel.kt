package com.mindeck.feature.card.createCard

import com.mindeck.core.mvi.BaseViewModel
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.DeckColor
import com.mindeck.domain.usecases.deck.command.CreateDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.domain.usecases.media.DownloadImageUseCase
import com.mindeck.feature.card.model.AudioSource
import com.mindeck.feature.card.model.DraftImage
import com.mindeck.feature.card.model.MediaSheet
import com.mindeck.feature.card.model.PhotoSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CreateCardViewModel(
    private val getAllDecksUseCase: GetAllDecksUseCase,
    private val createDeckUseCase: CreateDeckUseCase,
    private val downloadImageUseCase: DownloadImageUseCase,
) : BaseViewModel<CreateCardState, CreateCardIntent, CreateCardEffect>(CreateCardState()) {
    override fun accept(intent: CreateCardIntent) {
        when (intent) {
            CreateCardIntent.ClearDeck ->
                updateState { it.copy(pickedDeck = null) }

            CreateCardIntent.ShowNewDeckDialog ->
                updateState { it.copy(isNewDeckDialogVisible = true) }

            CreateCardIntent.DismissNewDeckDialog ->
                updateState { it.copy(isNewDeckDialogVisible = false) }

            is CreateCardIntent.CreateDeck -> createDeck(intent.name, intent.color)

            is CreateCardIntent.PickDeck ->
                updateState { state ->
                    state.copy(
                        pickedDeck = state.decks.find { it.id == intent.deckId },
                        isDeckPickerExpanded = false,
                    )
                }

            CreateCardIntent.ExpandDeckPicker ->
                updateState { it.copy(isDeckPickerExpanded = true) }

            is CreateCardIntent.SelectType ->
                updateState { it.copy(selectedType = intent.type) }

            is CreateCardIntent.UpdateQuestion ->
                updateState { it.copy(question = intent.value) }

            is CreateCardIntent.UpdateAnswer ->
                updateState { it.copy(answer = intent.value) }

            is CreateCardIntent.UpdateHint ->
                updateState { it.copy(hint = intent.value.ifBlank { null }) }

            is CreateCardIntent.ToggleFormat ->
                updateState { state ->
                    val formats =
                        if (intent.format in state.activeFormats) {
                            state.activeFormats - intent.format
                        } else {
                            state.activeFormats + intent.format
                        }
                    state.copy(activeFormats = formats)
                }

            CreateCardIntent.RemoveAudio ->
                updateState { it.copy(selectedAudio = null) }

            CreateCardIntent.RemoveImage ->
                updateState { it.copy(draftImage = null) }

            CreateCardIntent.ShowAddPhotoSheet ->
                updateState { it.copy(activeSheet = MediaSheet.PHOTO) }

            CreateCardIntent.ShowAddAudioSheet ->
                updateState { it.copy(activeSheet = MediaSheet.AUDIO) }

            CreateCardIntent.DismissSheet -> closeSheet()

            is CreateCardIntent.PickPhotoSource -> pickPhotoSource(intent.source)

            is CreateCardIntent.PickAudioSource -> pickAudioSource(intent.source)

            is CreateCardIntent.UpdateLink ->
                updateState { it.copy(linkDraft = intent.value) }

            CreateCardIntent.ConfirmLink -> confirmLink()

            CreateCardIntent.Submit -> submit()
        }
    }

    init {
        observeDecks()
    }

    private fun observeDecks() =
        getAllDecksUseCase()
            .onEach { decks -> updateState { it.copy(decks = decks.map { deck -> deck.toUi() }) } }
            .catch { updateState { it.copy(decks = emptyList()) } }
            .launchIn(viewModelScope)

    private fun createDeck(
        name: String,
        color: DeckColor,
    ) {
        viewModelScope.launch {
            val deck = Deck(deckName = name, deckColor = color)
            try {
                val newId = createDeckUseCase(deck)
                updateState {
                    it.copy(
                        pickedDeck = deck.copy(deckId = newId).toUi(),
                        isNewDeckDialogVisible = false,
                        isDeckPickerExpanded = false,
                    )
                }
            } catch (e: DomainError.NameAlreadyExists) {
                sendEffect(CreateCardEffect.CreationFailed(CreateCardError.DeckNameTaken))
            } catch (e: DomainError.DatabaseError) {
                updateState { it.copy(isNewDeckDialogVisible = false) }
                sendEffect(CreateCardEffect.CreationFailed(CreateCardError.Unknown))
            }
        }
    }

    private fun pickPhotoSource(source: PhotoSource) {
        closeSheet()
    }

    private fun pickAudioSource(source: AudioSource) {
        closeSheet()
    }

    private fun confirmLink() {
        val url = currentState.linkDraft.trim()
        val sheet = currentState.activeSheet
        closeSheet()
        if (url.isBlank()) return
        when (sheet) {
            MediaSheet.PHOTO -> downloadImage(url)
            MediaSheet.AUDIO -> Unit // audio-by-link arrives in a later slice
            null -> Unit
        }
    }

    private fun downloadImage(url: String) {
        updateState { it.copy(isDownloadingImage = true) }
        viewModelScope.launch {
            try {
                val bytes = downloadImageUseCase(url)
                updateState {
                    it.copy(draftImage = DraftImage(url = url, bytes = bytes), isDownloadingImage = false)
                }
            } catch (e: DomainError) {
                updateState { it.copy(isDownloadingImage = false) }
                sendEffect(CreateCardEffect.CreationFailed(CreateCardError.ImageDownloadFailed))
            }
        }
    }

    private fun closeSheet() = updateState { it.copy(activeSheet = null, linkDraft = "") }

    private fun submit() {
        if (!currentState.canSubmit) return
        updateState {
            it.copy(
                isSubmitting = false,
                question = "",
                answer = "",
                hint = null,
                activeFormats = emptySet(),
                draftImage = null,
                selectedAudio = null,
            )
        }
        sendEffect(CreateCardEffect.CardCreated)
    }
}
