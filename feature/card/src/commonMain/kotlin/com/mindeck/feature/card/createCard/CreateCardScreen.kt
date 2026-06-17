package com.mindeck.feature.card.createCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mindeck.core.ui.appBar.AppBar
import com.mindeck.core.ui.appBar.AppBarAction
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
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.arrow_back_icon
import mindeck_app.feature.card.generated.resources.create_card_action_back
import mindeck_app.feature.card.generated.resources.create_card_title
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.card.generated.resources.Res as CreateCardRes

@Composable
fun CreateCardScreen(
    state: CreateCardUiState,
    onIntent: (CreateCardIntent) -> Unit,
    onNavigate: (CreateCardNavigationEvent) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
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
        when (state) {
            is CreateCardUiState.Error -> TODO()
            is CreateCardUiState.Idle -> {
                CreateCardForm(
                    deckPick = state.deckPick,
                    selectedType = state.selectedType,
                    onSelectType = { onIntent(CreateCardIntent.SelectType(it)) },
                    question = state.question,
                    onQuestionChange = { onIntent(CreateCardIntent.UpdateQuestion(it)) },
                    onPickDeck = { onIntent(CreateCardIntent.PickDeck) },
                    onClearDeck = { onIntent(CreateCardIntent.ClearDeck) },
                    onCreateDeck = { onNavigate(CreateCardNavigationEvent.CreateDeck) },
                    modifier = Modifier.weight(1f),
                )
            }

            CreateCardUiState.Loading -> TODO()
            CreateCardUiState.Success -> TODO()
        }
    }
}

@Composable
private fun CreateCardForm(
    deckPick: DeckPickState,
    selectedType: CardType,
    onSelectType: (CardType) -> Unit,
    question: String,
    onQuestionChange: (String) -> Unit,
    onPickDeck: () -> Unit,
    onClearDeck: () -> Unit,
    onCreateDeck: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingLg)
    ) {
        DeckSection(
            state = deckPick,
            onPick = onPickDeck,
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
            value = question,
            onValueChange = onQuestionChange,
        )
        var activeFormats by remember { mutableStateOf(emptySet<TextFormat>()) }
        TextFormattingToolbar(
            active = activeFormats,
            onToggle = { format ->
                activeFormats =
                    if (format in activeFormats) activeFormats - format else activeFormats + format
            },
        )
        var selectedAudio by remember { mutableStateOf<String?>("null") }
        AudioPick(
            selectedAudio = selectedAudio,
            onPickFile = {},
            onRemoveAudio = { selectedAudio = null },
        )
        HintBlock(
            value = question,
            onValueChange = onQuestionChange,
        )
    }
}
