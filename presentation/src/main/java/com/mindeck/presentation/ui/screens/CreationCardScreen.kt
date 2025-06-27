package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.CardState
import com.mindeck.presentation.state.RenderUiState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.buttons.SaveDataButton
import com.mindeck.presentation.ui.components.dropdown.dropdown_selector.DropdownSelector
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.components.textfields.TegInputField
import com.mindeck.presentation.ui.components.textfields.TitleInputField
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.textInputModifier
import com.mindeck.presentation.ui.theme.text_gray
import com.mindeck.presentation.ui.theme.text_white
import com.mindeck.presentation.viewmodel.CreationCardViewModel

@Composable
fun CreationCardScreen(
    navController: NavController,
    deckId: Int?
) {
    val creationCardViewModel: CreationCardViewModel =
        hiltViewModel(navController.currentBackStackEntry!!)

    val cardState by creationCardViewModel.cardState
    val validation by creationCardViewModel.validation.collectAsState()
    val deck by creationCardViewModel.listDecksUiState.collectAsState()
    val selectedDeckForCreatingCard by creationCardViewModel.selectedDeckForCreatingCard
    val selectedTypeForCreatingCard by creationCardViewModel.selectedTypeForCreatingCard

    val typeDropdownList = dropdownMenuTypeList()

    LaunchedEffect(Unit) {
        creationCardViewModel.getAllDecks()
        if (deckId != null)
            creationCardViewModel.getDeckById(deckId)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Scaffold(
            topBar = {
                TopBar(onClick = { navController.popBackStack() })
            },
            content = { padding ->
                PageContent(
                    navController = navController,
                    padding = padding,
                    deck = deck,
                    typeDropdownList = typeDropdownList,
                    validation = validation,
                    cardState = cardState,
                    creationCardViewModel = creationCardViewModel,
                    selectedDeckForCreatingCard = selectedDeckForCreatingCard,
                    selectedTypeForCreatingCard = selectedTypeForCreatingCard,
                    {
                        creationCardViewModel.selectedDeckForCreatingCard.value = it
                    },
                    {
                        creationCardViewModel.selectedTypeForCreatingCard.value = it
                    }
                )
            }
        )
    }
}

@Composable
private fun TopBar(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_large))
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
private fun PageContent(
    navController: NavController,
    padding: PaddingValues,
    deck: UiState<List<Deck>>,
    typeDropdownList: List<Pair<String, Int>>,
    validation: Boolean?,
    cardState: CardState,
    creationCardViewModel: CreationCardViewModel,
    selectedDeckForCreatingCard: Pair<String, Int?>,
    selectedTypeForCreatingCard: Pair<String, Int?>,
    onSelectedDeckForCreatingCard: (Pair<String, Int>) -> Unit = { },
    onSelectedTypeForCreatingCard: (Pair<String, Int>) -> Unit = { }
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .navigationBarsPadding()
    ) {
        deck.RenderUiState(
            onSuccess = { deckInfo ->
                DropdownSelector(
                    "Выберите колоду",
                    selectedDeckForCreatingCard,
                    deckInfo.map { Pair<String, Int>(it.deckName, it.deckId) },
                    onItemClick = onSelectedDeckForCreatingCard,
                    onClick = {},
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            },
            onError = {
                Text("error")
            },
            onLoading = {
                Text("loading")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownSelector(
            "Выберите тип",
            selectedTypeForCreatingCard,
            typeDropdownList,
            onItemClick = onSelectedTypeForCreatingCard,
            onClick = {},
            textStyle = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
        InputFields(
            validation = validation == true || validation == null,
            cardState = cardState,
            creationCardViewModel = creationCardViewModel
        )
        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_large)))
        SaveButton(
            onClick = {
                if (creationCardViewModel.validateInput() ) {
                    selectedDeckForCreatingCard.second?.let {
                        creationCardViewModel.createCard(
                            cardName = cardState.title,
                            cardQuestion = cardState.question,
                            cardAnswer = cardState.answer,
                            cardType = selectedDeckForCreatingCard.second.toString(),
                            cardTag = cardState.tag,
                            deckId = it
                        )
                    }
                    navController.popBackStack()
                }
            }
        )
    }
}

@Composable
private fun dropdownMenuTypeList(): List<Pair<String, Int>> {
    return listOf(
        Pair(stringResource(R.string.text_folder_dropdown_selector_simple), 0),
        Pair(stringResource(R.string.text_folder_dropdown_selector_simple_with_answer_input), 1)
    )
}

@Composable
private fun InputFields(
    validation: Boolean,
    cardState: CardState,
    creationCardViewModel: CreationCardViewModel
) {
    TitleInputField(
        placeholder = stringResource(R.string.enter_name_for_card),
        value = cardState.title,
        singleLine = true,
        onValueChange = { creationCardViewModel.updateCardState { copy(title = it) } },
        readOnly = false,
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
            color = if (cardState.title.isNotBlank() || validation) text_gray else MaterialTheme.colorScheme.error
        ),
        modifier = textInputModifier(
            backgroundColor = if (cardState.title.isNotBlank() || validation) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onError,
            size = dimenDpResource(R.dimen.text_input_size_padding)
        )
            .fillMaxWidth()
            .heightIn(min = dimenDpResource(R.dimen.text_input_min_height))
            .wrapContentSize(Alignment.CenterStart)
    )
    Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
    CardInputField(
        placeholder = stringResource(R.string.enter_question_for_card),
        value = cardState.question,
        onValueChange = { creationCardViewModel.updateCardState { copy(question = it) } },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
            color = if (cardState.question.isNotBlank() || validation) text_gray else MaterialTheme.colorScheme.error
        ),
        modifier = textInputModifier(
            backgroundColor = if (cardState.question.isNotBlank() || validation) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onError,
            topStart = dimenDpResource(R.dimen.text_input_topStart_padding),
            topEnd = dimenDpResource(R.dimen.text_input_topEnd_padding),
            bottomStart = dimenDpResource(R.dimen.text_input_bottomStart_zero_padding),
            bottomEnd = dimenDpResource(R.dimen.text_input_bottomEnd_zero_padding)
        )
            .fillMaxWidth()
            .heightIn(
                min = dimenDpResource(R.dimen.text_input_min_height),
                max = dimenDpResource(R.dimen.text_input_max_height)
            )
            .wrapContentSize(Alignment.CenterStart)
    )
    CardInputField(
        placeholder = stringResource(R.string.enter_answer_for_card),
        value = cardState.answer,
        onValueChange = { creationCardViewModel.updateCardState { copy(answer = it) } },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
            color = if (cardState.answer.isNotBlank() || validation) text_gray else MaterialTheme.colorScheme.error
        ),
        modifier = textInputModifier(
            backgroundColor = if (cardState.answer.isNotBlank() || validation) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onError,
            topEnd = dimenDpResource(R.dimen.text_input_topEnd_zero_padding),
            topStart = dimenDpResource(R.dimen.text_input_topStart_zero_padding),
            bottomStart = dimenDpResource(R.dimen.text_input_topStart_padding),
            bottomEnd = dimenDpResource(R.dimen.text_input_topStart_padding)
        )
            .fillMaxWidth()
            .heightIn(
                min = dimenDpResource(R.dimen.text_input_min_height),
                max = dimenDpResource(R.dimen.text_input_max_height)
            )
            .wrapContentSize(Alignment.CenterStart),

        )
    Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
    TegInputField(
        titleTextInput = stringResource(R.string.text_tag_input_field),
        value = cardState.tag,
        onValueChange = { creationCardViewModel.updateCardState { copy(tag = it) } },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
            color = text_gray
        ),
        modifier = textInputModifier(size = dimenDpResource(R.dimen.text_input_size_padding))
            .size(
                width = dimenDpResource(R.dimen.tag_text_input_min_weight),
                height = dimenDpResource(R.dimen.text_input_min_height)
            )
            .wrapContentSize(Alignment.CenterStart)
    )
}

@Composable
private fun SaveButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        SaveDataButton(
            text = stringResource(R.string.text_save_card_button),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = text_white
            ),
            buttonModifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.small
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onClick()
                }
        )
    }
}

