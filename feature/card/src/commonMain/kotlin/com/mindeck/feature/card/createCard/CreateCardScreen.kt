package com.mindeck.feature.card.createCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mindeck.core.ui.appBar.AppBar
import com.mindeck.core.ui.appBar.AppBarAction
import com.mindeck.core.ui.button.AppButton
import com.mindeck.core.ui.effect.ObserveEffects
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.card.components.AnswerBlock
import com.mindeck.feature.card.components.AudioPick
import com.mindeck.feature.card.components.CardTypeSection
import com.mindeck.feature.card.components.DeckSection
import com.mindeck.feature.card.components.HintBlock
import com.mindeck.feature.card.components.PhotoPick
import com.mindeck.feature.card.components.QuestionBlock
import com.mindeck.feature.card.components.TextFormattingToolbar
import com.mindeck.feature.card.model.CardType
import com.mindeck.feature.card.model.DeckPickState
import com.mindeck.feature.card.model.TextFormat
import kotlinx.coroutines.flow.Flow
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.arrow_back_icon
import mindeck_app.core.ui.generated.resources.check_icon
import mindeck_app.feature.card.generated.resources.create_card_action_back
import mindeck_app.feature.card.generated.resources.create_card_action_create
import mindeck_app.feature.card.generated.resources.create_card_created
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
    ObserveEffects(effects) { effect ->
        val message =
            when (effect) {
                CreateCardEffect.CardCreated -> cardCreatedMessage
                is CreateCardEffect.CreationFailed -> effect.message
            }
        snackbarHostState.showSnackbar(message)
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(MindeckTheme.dimensions.screenPadding),
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
            deckPick = state.deckPick,
            selectedType = state.selectedType,
            onSelectType = { onIntent(CreateCardIntent.SelectType(it)) },
            question = state.question,
            onQuestionChange = { onIntent(CreateCardIntent.UpdateQuestion(it)) },
            answer = state.answer,
            onAnswerChange = { onIntent(CreateCardIntent.UpdateAnswer(it)) },
            hint = state.hint.orEmpty(),
            onHintChange = { onIntent(CreateCardIntent.UpdateHint(it)) },
            activeFormats = state.activeFormats,
            onToggleFormat = { onIntent(CreateCardIntent.ToggleFormat(it)) },
            selectedAudio = state.selectedAudio,
            onRemoveAudio = { onIntent(CreateCardIntent.RemoveAudio) },
            canCreate = state.canSubmit,
            onCreate = { onIntent(CreateCardIntent.Submit) },
            onPickDeck = { onIntent(CreateCardIntent.PickDeck(it)) },
            onClearDeck = { onIntent(CreateCardIntent.ClearDeck) },
            onCreateDeck = { onNavigate(CreateCardNavigationEvent.CreateDeck) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun CreateCardForm(
    deckPick: DeckPickState,
    selectedType: CardType,
    onSelectType: (CardType) -> Unit,
    question: String,
    onQuestionChange: (String) -> Unit,
    answer: String,
    onAnswerChange: (String) -> Unit,
    hint: String,
    onHintChange: (String) -> Unit,
    activeFormats: Set<TextFormat>,
    onToggleFormat: (TextFormat) -> Unit,
    selectedAudio: String?,
    onRemoveAudio: () -> Unit,
    canCreate: Boolean,
    onCreate: () -> Unit,
    onPickDeck: (Int) -> Unit,
    onClearDeck: () -> Unit,
    onCreateDeck: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingLg),
    ) {
        DeckSection(
            state = deckPick,
            onPickDeck = onPickDeck,
            onClear = onClearDeck,
            onCreateDeck = onCreateDeck,
        )
        CardTypeSection(
            selectedType = selectedType,
            onSelectType = onSelectType,
        )
        PhotoPick()
        QuestionBlock(
            value = question,
            onValueChange = onQuestionChange,
        )
        AnswerBlock(
            value = answer,
            onValueChange = onAnswerChange,
        )
        TextFormattingToolbar(
            active = activeFormats,
            onToggle = onToggleFormat,
        )
        AudioPick(
            selectedAudio = selectedAudio,
            onPickFile = { /* TODO: platform audio picker */ },
            onRemoveAudio = onRemoveAudio,
        )
        HintBlock(
            value = hint,
            onValueChange = onHintChange,
        )
        AppButton(
            onAction = onCreate,
            buttonText = stringResource(CreateCardRes.string.create_card_action_create),
            buttonIcon = Res.drawable.check_icon,
            color = MaterialTheme.colorScheme.primary,
            enabled = canCreate,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
