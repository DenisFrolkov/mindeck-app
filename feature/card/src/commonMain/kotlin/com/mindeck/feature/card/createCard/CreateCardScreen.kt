package com.mindeck.feature.card.createCard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.mindeck.core.ui.appBar.AppBar
import com.mindeck.core.ui.appBar.AppBarAction
import com.mindeck.core.ui.button.AppButton
import com.mindeck.core.ui.button.appButtonColors
import com.mindeck.core.ui.dialog.CreateDeckDialog
import com.mindeck.core.ui.effect.ObserveEffects
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.domain.models.DeckColor
import com.mindeck.feature.card.components.AnswerBlock
import com.mindeck.feature.card.components.CameraCaptureScreen
import com.mindeck.feature.card.components.CardTypeSection
import com.mindeck.feature.card.components.DeckSection
import com.mindeck.feature.card.components.HintBlock
import com.mindeck.feature.card.components.MediaPickerSheet
import com.mindeck.feature.card.components.PhotoPick
import com.mindeck.feature.card.components.QuestionBlock
import com.mindeck.feature.card.components.cameraCaptureImage
import com.mindeck.feature.card.components.rememberImagePickers
import com.mindeck.feature.card.model.CardTextFieldState
import com.mindeck.feature.card.model.CardTypeFieldState
import com.mindeck.feature.card.model.DeckFieldState
import com.mindeck.feature.card.model.LinkFieldState
import com.mindeck.feature.card.model.MediaFieldState
import kotlinx.coroutines.flow.Flow
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.arrow_back_icon
import mindeck_app.core.ui.generated.resources.check_icon
import mindeck_app.feature.card.generated.resources.create_card_action_back
import mindeck_app.feature.card.generated.resources.create_card_action_cancel
import mindeck_app.feature.card.generated.resources.create_card_action_submit
import mindeck_app.feature.card.generated.resources.create_card_created
import mindeck_app.feature.card.generated.resources.create_card_deck_create_failed
import mindeck_app.feature.card.generated.resources.create_card_deck_name_taken
import mindeck_app.feature.card.generated.resources.create_card_duplicate
import mindeck_app.feature.card.generated.resources.create_card_image_attach_failed
import mindeck_app.feature.card.generated.resources.create_card_image_download_failed
import mindeck_app.feature.card.generated.resources.create_card_open
import mindeck_app.feature.card.generated.resources.create_card_save_failed
import mindeck_app.feature.card.generated.resources.create_card_title
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.card.generated.resources.Res as CreateCardRes

@Composable
fun CreateCardScreen(
    state: CreateCardState,
    effects: Flow<CreateCardEffect>,
    onIntent: (CreateCardIntent) -> Unit,
    onNavigate: (CreateCardNavigationEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val cardCreatedMessage = stringResource(CreateCardRes.string.create_card_created)
    val openCardLabel = stringResource(CreateCardRes.string.create_card_open)
    val deckNameTakenMessage = stringResource(CreateCardRes.string.create_card_deck_name_taken)
    val deckCreateFailedMessage = stringResource(CreateCardRes.string.create_card_deck_create_failed)
    val imageDownloadFailedMessage = stringResource(CreateCardRes.string.create_card_image_download_failed)
    val imageAttachFailedMessage = stringResource(CreateCardRes.string.create_card_image_attach_failed)
    val duplicateCardMessage = stringResource(CreateCardRes.string.create_card_duplicate)
    val saveFailedMessage = stringResource(CreateCardRes.string.create_card_save_failed)
    ObserveEffects(effects) { effect ->
        when (effect) {
            CreateCardEffect.CardCreated -> {
                val result =
                    snackbarHostState.showSnackbar(
                        message = cardCreatedMessage,
                        actionLabel = openCardLabel,
                        duration = SnackbarDuration.Short,
                    )
                if (result == SnackbarResult.ActionPerformed) {
                    // TODO: navigate to the card details screen once it exists (onNavigate(...)).
                }
            }

            is CreateCardEffect.CreationFailed -> {
                val message =
                    when (effect.reason) {
                        CreateCardError.DeckNameTaken -> deckNameTakenMessage
                        CreateCardError.ImageDownloadFailed -> imageDownloadFailedMessage
                        CreateCardError.ImageAttachFailed -> imageAttachFailedMessage
                        CreateCardError.DuplicateCard -> duplicateCardMessage
                        CreateCardError.SaveFailed -> saveFailedMessage
                        CreateCardError.Unknown -> deckCreateFailedMessage
                    }
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    val imagePickers =
        rememberImagePickers(
            onPickStart = { onIntent(CreateCardIntent.BeginImagePick) },
            onPicked = { onIntent(CreateCardIntent.AttachImage(it)) },
            onPickFailed = { onIntent(CreateCardIntent.FailImagePick) },
            onShowCamera = { onIntent(CreateCardIntent.ShowCamera) },
        )

    val actionBarHeight =
        MindeckTheme.dimensions.touchTarget + MindeckTheme.dimensions.spacingMd * 2

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding),
    ) {
        Column(
            modifier = Modifier.padding(MindeckTheme.dimensions.screenPadding),
        ) {
            AppBar(
                title = stringResource(CreateCardRes.string.create_card_title),
                navigation =
                    AppBarAction(
                        icon = Res.drawable.arrow_back_icon,
                        contentDescription = stringResource(CreateCardRes.string.create_card_action_back),
                        iconColor = MaterialTheme.colorScheme.onSurface,
                        onClick = { onNavigate(CreateCardNavigationEvent.Back) },
                    ),
                modifier = Modifier.statusBarsPadding(),
            )
            VerticalSpacer(MindeckTheme.dimensions.spacingXl)
            CreateCardForm(
                deck =
                    DeckFieldState(
                        deckPick = state.deckPick,
                        expanded = state.isDeckPickerExpanded,
                        onPickDeck = { onIntent(CreateCardIntent.PickDeck(it)) },
                        onClear = { onIntent(CreateCardIntent.ClearDeck) },
                        onCreateDeck = { onIntent(CreateCardIntent.ShowNewDeckDialog) },
                        onExpand = { onIntent(CreateCardIntent.ExpandDeckPicker) },
                    ),
                type =
                    CardTypeFieldState(
                        selectedType = state.selectedType,
                        onSelectType = { onIntent(CreateCardIntent.SelectType(it)) },
                    ),
                text =
                    CardTextFieldState(
                        question = state.question,
                        onQuestionChange = { onIntent(CreateCardIntent.UpdateQuestion(it)) },
                        answer = state.answer,
                        onAnswerChange = { onIntent(CreateCardIntent.UpdateAnswer(it)) },
                        hint = state.hint.orEmpty(),
                        onHintChange = { onIntent(CreateCardIntent.UpdateHint(it)) },
                        activeFormats = state.activeFormats,
                        onToggleFormat = { onIntent(CreateCardIntent.ToggleFormat(it)) },
                    ),
                media =
                    MediaFieldState(
                        image = state.draftImage,
                        isImageLoading = state.isProcessingImage,
                        onAddPhoto = { onIntent(CreateCardIntent.ShowAddPhotoSheet) },
                        onRemoveImage = { onIntent(CreateCardIntent.RemoveImage) },
                        onCancelLoad = {
                            onIntent(CreateCardIntent.CancelLoadImage)
                            imagePickers.cancel()
                        },
                    ),
                bottomInset = actionBarHeight,
                modifier = Modifier.weight(1f),
            )
        }

        AnimatedVisibility(
            visible = state.canSubmit,
            enter = slideInVertically { height -> height } + fadeIn(),
            exit = slideOutVertically { height -> height } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            CreateCardActionBar(
                onCancel = { onNavigate(CreateCardNavigationEvent.Back) },
                onCreateCard = { onIntent(CreateCardIntent.Submit) },
                isProcessingImage = state.isProcessingImage,
            )
        }

        if (state.isNewDeckDialogVisible) {
            CreateDeckDialog(
                colors = deckSwatchColors(),
                onDismiss = { onIntent(CreateCardIntent.DismissNewDeckDialog) },
                onConfirm = { name, colorIndex ->
                    onIntent(CreateCardIntent.CreateDeck(name, DeckColor.entries[colorIndex]))
                },
            )
        }

        state.activeSheet?.let { sheet ->
            MediaPickerSheet(
                sheet = sheet,
                link =
                    LinkFieldState(
                        draft = state.linkDraft,
                        onChange = { onIntent(CreateCardIntent.UpdateLink(it)) },
                        onConfirm = { onIntent(CreateCardIntent.ConfirmLink) },
                    ),
                onDismiss = { onIntent(CreateCardIntent.DismissSheet) },
                onPickPhotoSource = { source ->
                    onIntent(CreateCardIntent.DismissSheet)
                    imagePickers.launch(source)
                },
            )
        }

        if (state.isCameraVisible) {
            CameraCaptureScreen(
                onCaptured = { bytes ->
                    val image = cameraCaptureImage(bytes)
                    if (image != null) {
                        onIntent(CreateCardIntent.CameraCaptured(state.cameraSessionId, image))
                    } else {
                        onIntent(CreateCardIntent.CameraCaptureFailed(state.cameraSessionId))
                    }
                },
                onFailed = {
                    onIntent(CreateCardIntent.CameraCaptureFailed(state.cameraSessionId))
                },
                onDismiss = { onIntent(CreateCardIntent.DismissCamera) },
            )
        }
    }
}

@Composable
private fun CreateCardForm(
    deck: DeckFieldState,
    type: CardTypeFieldState,
    text: CardTextFieldState,
    media: MediaFieldState,
    bottomInset: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingLg),
    ) {
        DeckSection(
            state = deck.deckPick,
            expanded = deck.expanded,
            onPickDeck = deck.onPickDeck,
            onClear = deck.onClear,
            onCreateDeck = deck.onCreateDeck,
            onExpand = deck.onExpand,
        )
        CardTypeSection(
            selectedType = type.selectedType,
            onSelectType = type.onSelectType,
        )
        PhotoPick(
            image = media.image,
            isLoading = media.isImageLoading,
            onClick = media.onAddPhoto,
            onRemove = media.onRemoveImage,
            onCancelLoad = media.onCancelLoad,
        )
        QuestionBlock(
            value = text.question,
            onValueChange = text.onQuestionChange,
        )
        AnswerBlock(
            value = text.answer,
            onValueChange = text.onAnswerChange,
        )
//        TextFormattingToolbar(
//            active = text.activeFormats,
//            onToggle = text.onToggleFormat,
//        )
        HintBlock(
            value = text.hint,
            onValueChange = text.onHintChange,
        )
        VerticalSpacer(bottomInset)
    }
}

@Composable
private fun CreateCardActionBar(
    onCancel: () -> Unit,
    onCreateCard: () -> Unit,
    isProcessingImage: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                .navigationBarsPadding()
                .padding(
                    horizontal = MindeckTheme.dimensions.screenPadding,
                    vertical = MindeckTheme.dimensions.spacingMd,
                ).heightIn(min = MindeckTheme.dimensions.touchTarget),
        horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingMd),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppButton(
            onAction = onCancel,
            buttonText = stringResource(CreateCardRes.string.create_card_action_cancel),
            modifier = Modifier.weight(0.50f),
            colors =
                appButtonColors(
                    container = MaterialTheme.colorScheme.secondaryContainer,
                    content = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
        )

        AppButton(
            onAction = onCreateCard,
            enabled = !isProcessingImage,
            buttonText = stringResource(CreateCardRes.string.create_card_action_submit),
            modifier = Modifier.weight(0.75f),
            buttonIcon = Res.drawable.check_icon,
        )
    }
}
