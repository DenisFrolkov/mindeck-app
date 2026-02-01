package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.CardState
import com.mindeck.presentation.state.RenderUiState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.buttons.SaveDataButton
import com.mindeck.presentation.ui.components.dropdown.dropdownSelector.DropdownSelector
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.components.textfields.TegInputField
import com.mindeck.presentation.ui.components.textfields.TitleInputField
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.textInputModifier
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.ui.theme.text_gray
import com.mindeck.presentation.ui.theme.text_white
import com.mindeck.presentation.viewmodel.CreationCardViewModel

@Composable
fun CreationCardScreen(
    navController: NavController,
    deckId: Int?,
) {
    val creationCardViewModel: CreationCardViewModel =
        hiltViewModel(navController.currentBackStackEntry!!)

    val cardState by creationCardViewModel.cardState
    val validation by creationCardViewModel.validation.collectAsState()
    val deck by creationCardViewModel.listDecksUiState.collectAsState()
    val selectedDeckForCreatingCard by creationCardViewModel.selectedDeckForCreatingCard
    val selectedTypeForCreatingCard by creationCardViewModel.selectedTypeForCreatingCard

    LaunchedEffect(Unit) {
        creationCardViewModel.getAllDecks()
        if (deckId != null) {
            creationCardViewModel.getDeckById(deckId)
        }
    }

    CreationCardContent(
        navController,
        deck,
        validation,
        cardState,
        selectedDeckForCreatingCard,
        selectedTypeForCreatingCard,
        {
            creationCardViewModel.selectedDeckForCreatingCard.value = it
        },
        {
            creationCardViewModel.selectedTypeForCreatingCard.value = it
        },
        onCardNameSave = {
            creationCardViewModel.updateCardState { copy(title = it) }
        },
        onCardQuestionSave = {
            creationCardViewModel.updateCardState { copy(question = it) }
        },
        onCardAnswerSave = {
            creationCardViewModel.updateCardState { copy(answer = it) }
        },
        onCardTagSave = {
            creationCardViewModel.updateCardState { copy(tag = it) }
        },
        onSaveCard = {
            if (creationCardViewModel.validateInput()) {
                selectedDeckForCreatingCard.second?.let {
                    creationCardViewModel.createCard(
                        cardName = cardState.title,
                        cardQuestion = cardState.question,
                        cardAnswer = cardState.answer,
                        cardType = selectedDeckForCreatingCard.second.toString(),
                        cardTag = cardState.tag,
                        deckId = it,
                    )
                }
                navController.popBackStack()
            }
        },
    )
}

@Composable
private fun CreationCardContent(
    navController: NavController,
    decks: UiState<List<Deck>>,
    validation: Boolean?,
    cardState: CardState,
    selectedDeckForCreatingCard: Pair<String, Int?>,
    selectedTypeForCreatingCard: Pair<String, Int?>,
    onSelectedDeckForCreatingCard: (Pair<String, Int>) -> Unit = { },
    onSelectedTypeForCreatingCard: (Pair<String, Int>) -> Unit = { },
    onCardNameSave: (String) -> Unit,
    onCardQuestionSave: (String) -> Unit,
    onCardAnswerSave: (String) -> Unit,
    onCardTagSave: (String) -> Unit,
    onSaveCard: () -> Unit,
) {
    val typeDropdownList = dropdownMenuTypeList()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(onClick = { navController.popBackStack() })
        },
        content = { padding ->
            Content(
                padding = padding,
                deck = decks,
                typeDropdownList = typeDropdownList,
                validation = validation,
                cardState = cardState,
                selectedDeckForCreatingCard = selectedDeckForCreatingCard,
                selectedTypeForCreatingCard = selectedTypeForCreatingCard,
                onSelectedDeckForCreatingCard = onSelectedDeckForCreatingCard,
                onSelectedTypeForCreatingCard = onSelectedTypeForCreatingCard,
                onCardNameSave = onCardNameSave,
                onCardQuestionSave = onCardQuestionSave,
                onCardAnswerSave = onCardAnswerSave,
                onCardTagSave = onCardTagSave,
                onSaveCard = onSaveCard,
            )
        },
    )
}

@Composable
private fun TopBar(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_large)),
    ) {
        ActionHandlerButton(
            iconPainter = painterResource(R.drawable.back_icon),
            contentDescription = stringResource(R.string.back_screen_icon_button),
            onClick = onClick,
            iconTint = MaterialTheme.colorScheme.onPrimary,
        )
        Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_large)))
    }
}

@Composable
private fun Content(
    padding: PaddingValues,
    deck: UiState<List<Deck>>,
    typeDropdownList: List<Pair<String, Int>>,
    validation: Boolean?,
    cardState: CardState,
    selectedDeckForCreatingCard: Pair<String, Int?>,
    selectedTypeForCreatingCard: Pair<String, Int?>,
    onSelectedDeckForCreatingCard: (Pair<String, Int>) -> Unit = { },
    onSelectedTypeForCreatingCard: (Pair<String, Int>) -> Unit = { },
    onCardNameSave: (String) -> Unit,
    onCardQuestionSave: (String) -> Unit,
    onCardAnswerSave: (String) -> Unit,
    onCardTagSave: (String) -> Unit,
    onSaveCard: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .navigationBarsPadding(),
    ) {
        deck.RenderUiState(
            onSuccess = { deckInfo ->
                DropdownSelector(
                    "Выберите колоду",
                    selectedDeckForCreatingCard,
                    deckInfo.map { Pair<String, Int>(it.deckName, it.deckId) },
                    onItemClick = onSelectedDeckForCreatingCard,
                    onClick = {},
                    textStyle = MaterialTheme.typography.bodyMedium,
                )
            },
            onError = {
                Text("error")
            },
            onLoading = {
                Text("loading")
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownSelector(
            "Выберите тип",
            selectedTypeForCreatingCard,
            typeDropdownList,
            onItemClick = onSelectedTypeForCreatingCard,
            onClick = {},
            textStyle = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
        InputFields(
            validation = validation == true || validation == null,
            cardState = cardState,
            onCardNameSave = onCardNameSave,
            onCardQuestionSave = onCardQuestionSave,
            onCardAnswerSave = onCardAnswerSave,
            onCardTagSave = onCardTagSave,
        )
        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_large)))
        SaveButton(
            onClick = {
                onSaveCard()
            },
        )
    }
}

@Composable
private fun dropdownMenuTypeList(): List<Pair<String, Int>> {
    return listOf(
        Pair(stringResource(R.string.text_folder_dropdown_selector_simple), 0),
        Pair(stringResource(R.string.text_folder_dropdown_selector_simple_with_answer_input), 1),
    )
}

@Composable
private fun InputFields(
    validation: Boolean,
    cardState: CardState,
    onCardNameSave: (String) -> Unit,
    onCardQuestionSave: (String) -> Unit,
    onCardAnswerSave: (String) -> Unit,
    onCardTagSave: (String) -> Unit,
) {
    TitleInputField(
        placeholder = stringResource(R.string.enter_name_for_card),
        value = cardState.title,
        singleLine = true,
        onValueChange = {
            onCardNameSave(it)
        },
        readOnly = false,
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
            color = if (cardState.title.isNotBlank() || validation) text_gray else MaterialTheme.colorScheme.error,
        ),
        modifier = textInputModifier(
            backgroundColor = if (cardState.title.isNotBlank() || validation) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onError,
            size = dimenDpResource(R.dimen.text_input_size_padding),
        )
            .fillMaxWidth()
            .heightIn(min = dimenDpResource(R.dimen.text_input_min_height))
            .wrapContentSize(Alignment.CenterStart),
    )
    Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
    CardInputField(
        placeholder = stringResource(R.string.enter_question_for_card),
        value = cardState.question,
        onValueChange = {
            onCardQuestionSave(it)
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
            color = if (cardState.question.isNotBlank() || validation) text_gray else MaterialTheme.colorScheme.error,
        ),
        modifier = textInputModifier(
            backgroundColor = if (cardState.question.isNotBlank() || validation) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onError,
            topStart = dimenDpResource(R.dimen.text_input_topStart_padding),
            topEnd = dimenDpResource(R.dimen.text_input_topEnd_padding),
            bottomStart = dimenDpResource(R.dimen.text_input_bottomStart_zero_padding),
            bottomEnd = dimenDpResource(R.dimen.text_input_bottomEnd_zero_padding),
        )
            .fillMaxWidth()
            .heightIn(
                min = dimenDpResource(R.dimen.text_input_min_height),
                max = dimenDpResource(R.dimen.text_input_max_height),
            )
            .wrapContentSize(Alignment.CenterStart),
    )
    CardInputField(
        placeholder = stringResource(R.string.enter_answer_for_card),
        value = cardState.answer,
        onValueChange = {
            onCardAnswerSave(it)
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
            color = if (cardState.answer.isNotBlank() || validation) text_gray else MaterialTheme.colorScheme.error,
        ),
        modifier = textInputModifier(
            backgroundColor = if (cardState.answer.isNotBlank() || validation) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onError,
            topEnd = dimenDpResource(R.dimen.text_input_topEnd_zero_padding),
            topStart = dimenDpResource(R.dimen.text_input_topStart_zero_padding),
            bottomStart = dimenDpResource(R.dimen.text_input_topStart_padding),
            bottomEnd = dimenDpResource(R.dimen.text_input_topStart_padding),
        )
            .fillMaxWidth()
            .heightIn(
                min = dimenDpResource(R.dimen.text_input_min_height),
                max = dimenDpResource(R.dimen.text_input_max_height),
            )
            .wrapContentSize(Alignment.CenterStart),

    )
    Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
    TegInputField(
        titleTextInput = stringResource(R.string.text_tag_input_field),
        value = cardState.tag,
        onValueChange = {
            onCardTagSave(it)
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
            color = text_gray,
        ),
        modifier = textInputModifier(size = dimenDpResource(R.dimen.text_input_size_padding))
            .size(
                width = dimenDpResource(R.dimen.tag_text_input_min_weight),
                height = dimenDpResource(R.dimen.text_input_min_height),
            )
            .wrapContentSize(Alignment.CenterStart),
    )
}

@Composable
private fun SaveButton(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center),
    ) {
        SaveDataButton(
            text = stringResource(R.string.text_save_card_button),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = text_white,
            ),
            buttonModifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.small,
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    onClick()
                },
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFE6E6FF,
)
@Composable
private fun CreationCardContentScreenPreview() {
    val navController = rememberNavController()
    val decksState: UiState<List<Deck>> = decksDataMock()
    val cardState: CardState = CardState("", "", "", "", 1)

    MindeckTheme {
        CreationCardContent(
            navController,
            decksState,
            null,
            cardState,
            Pair("Выберите колоду", null),
            Pair("Выберите тип", null),
            {},
            {},
            {},
            {},
            {},
            {},
            {},
        )
    }
}

@Preview(
    device = "spec:parent=pixel_5,orientation=landscape",
    showBackground = true,
    backgroundColor = 0xFFE6E6FF,
)
@Composable
private fun CreationCardContentScreenPreviewLandscape() {
    val navController = rememberNavController()
    val decksState: UiState<List<Deck>> = decksDataMock()
    val cardState: CardState = CardState("", "", "", "", 1)

    MindeckTheme {
        CreationCardContent(
            navController,
            decksState,
            null,
            cardState,
            Pair("Выберите колоду", null),
            Pair("Выберите тип", null),
            {},
            {},
            {},
            {},
            {},
            {},
            {},
        )
    }
}

@Composable
private fun decksDataMock(): UiState<List<Deck>> = UiState.Success(
    listOf<Deck>(
        Deck(
            deckId = 1,
            deckName = "Kotlin Basics",
        ),
        Deck(
            deckId = 2,
            deckName = "Jetpack Compose",
        ),
        Deck(
            deckId = 3,
            deckName = "Architecture Patterns",
        ),
        Deck(
            deckId = 4,
            deckName = "Coroutines & Flow",
        ),
    ),
)
