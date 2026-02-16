package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.RenderState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemData
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemStyle
import com.mindeck.presentation.ui.components.dialog.CustomModalWindow
import com.mindeck.presentation.ui.components.dropdown.dropdownMenu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdownMenu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdownMenu.DropdownMenuState
import com.mindeck.presentation.ui.components.folder.DisplayItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.DeckRoute
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.navigation.Navigator
import com.mindeck.presentation.ui.navigation.StackNavigator
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.viewmodel.DecksViewModel

@Composable
fun DecksScreen(
    navigator: Navigator,
    decksViewModel: DecksViewModel = hiltViewModel<DecksViewModel>(),
) {
    val decks = decksViewModel.decksState.collectAsState().value
    val createDeckState by decksViewModel.createDeckState.collectAsState()
    val modalWindowValue by decksViewModel.modalWindowValue.collectAsState()

    LaunchedEffect(createDeckState) {
        if (createDeckState is UiState.Success) {
            decksViewModel.toggleModalWindow(false)
        }
    }

    val dropdownMenuState = remember { DropdownMenuState() }

    DecksContent(
        navigator,
        decks,
        createDeckState,
        dropdownMenuState,
        modalWindowValue,
        onSaveDeck = { deckName ->
            decksViewModel.createDeck(deckName)
        },
        toggleCreateDeckModalWindow = {
            decksViewModel.toggleModalWindow(it)
        },
    )
}

@Composable
private fun DecksContent(
    navigator: Navigator,
    decks: UiState<List<Deck>>,
    createDeckState: UiState<Unit>,
    dropdownMenuState: DropdownMenuState,
    initialDialog: Boolean = false,
    onSaveDeck: (String) -> Unit,
    toggleCreateDeckModalWindow: (Boolean) -> Unit,
) {
    val listDropdownMenu =
        dropdownMenuDataList {
            dropdownMenuState.toggle()
            toggleCreateDeckModalWindow(true)
        }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            DecksEditTopBar(navigator, dropdownMenuState)
        },
        content = { padding ->
            DecksContent(navigator, padding, decks, listDropdownMenu, dropdownMenuState)

            if (initialDialog) {
                Dialog(
                    onDismissRequest = {
                        toggleCreateDeckModalWindow(false)
                    },
                ) {
                    CustomModalWindow(
                        stringResource(R.string.create_item_dialog_text_creating_deck),
                        stringResource(R.string.create_item_dialog_text_create_deck),
                        stringResource(R.string.create_item_dialog_text_input_name_deck),
                        isLoading = createDeckState is UiState.Loading,
                        errorMsg = (createDeckState as UiState.Error).error,
                        onExitClick = {
                            toggleCreateDeckModalWindow(false)
                        },
                        onSaveClick = {
                            onSaveDeck(it)
                        },
                    )
                }
            }
        },
    )
}

@Composable
private fun dropdownMenuDataList(
    onCreateDeck: () -> Unit,
): List<DropdownMenuData> {
    return listOf(
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_create_deck_list),
            titleStyle = MaterialTheme.typography.bodyMedium,
            action = {
                onCreateDeck()
            },
        ),
    )
}

@Composable
private fun DecksEditTopBar(
    navigator: Navigator,
    dropdownMenuState: DropdownMenuState,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ActionHandlerButton(
            iconPainter = painterResource(R.drawable.back_icon),
            contentDescription = stringResource(R.string.back_screen_icon_button),
            iconTint = MaterialTheme.colorScheme.onPrimary,
            onClick = { navigator.pop() },
        )
        ActionHandlerButton(
            iconPainter = painterResource(R.drawable.menu_icon),
            contentDescription = stringResource(R.string.back_screen_icon_button),
            iconTint = MaterialTheme.colorScheme.onPrimary,
            onClick = { dropdownMenuState.toggle() },
        )
    }
}

@Composable
private fun DecksContent(
    navigator: Navigator,
    padding: PaddingValues,
    decks: UiState<List<Deck>>,
    listDropdownMenu: List<DropdownMenuData>,
    dropdownMenuState: DropdownMenuState,
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .navigationBarsPadding(),
    ) {
        DecksInfo(decks, navigator)
    }

    if (dropdownMenuState.isExpanded) {
        DeckDropdownMenu(
            padding = padding,
            listDropdownMenu = listDropdownMenu,
            dropdownMenuState = dropdownMenuState,
        )
    }
}

@Composable
private fun DecksInfo(
    decksState: UiState<List<Deck>>,
    navigator: Navigator,
) {
    decksState.RenderState(
        onSuccess = { decks ->
            LazyColumn {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center),
                    ) {
                        Text(
                            text = pluralStringResource(
                                R.plurals.deck_amount,
                                decks.size,
                                decks.size,
                            ),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_large)))
                }
                items(
                    items = decks,
                    key = { it.deckId },
                ) { deck ->
                    DisplayItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        showCount = false,
                        displayItemData = DisplayItemData(
                            itemIcon = R.drawable.deck_icon,
                            numberOfCards = deck.deckId,
                            itemName = deck.deckName,
                        ),
                        displayItemStyle = DisplayItemStyle(
                            backgroundColor = MaterialTheme.colorScheme.secondary.copy(
                                dimenFloatResource(R.dimen.float_zero_dot_five_significance),
                            ),
                            iconColor = MaterialTheme.colorScheme.outlineVariant,
                            textStyle = MaterialTheme.typography.bodyMedium,
                        ),
                        onClick = {
                            navigator.push(DeckRoute(deck.deckId))
                        },
                    )
                    Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_small)))
                }
            }
        },
        onLoading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimenDpResource(R.dimen.padding_large))
                    .wrapContentSize(Alignment.Center),
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_one),
                )
            }
        },
        onError = {
            Text(
                stringResource(R.string.error_get_all_decks),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
            )
        },
    )
}

@Composable
private fun DeckDropdownMenu(
    padding: PaddingValues,
    listDropdownMenu: List<DropdownMenuData>,
    dropdownMenuState: DropdownMenuState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { dropdownMenuState.toggle() },
    )

    DropdownMenu(
        listDropdownMenuItem = listDropdownMenu,
        dropdownModifier = Modifier
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(padding)
            .fillMaxWidth()
            .padding(top = dimenDpResource(R.dimen.spacer_extra_small))
            .wrapContentSize(Alignment.TopEnd),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFE6E6FF,
)
@Composable
private fun ScreenPreview() {
    val backStack = remember { mutableStateListOf<NavigationRoute>(DeckRoute(1)) }
    val navigator = remember { StackNavigator(backStack) }
    val decksState: UiState<List<Deck>> = decksDataMock()
    val dropdownMenuState = remember { DropdownMenuState() }

    MindeckTheme {
        DecksContent(
            navigator,
            decksState,
            UiState.Loading,
            dropdownMenuState,
            false,
            { },
            { _ -> },
        )
    }
}

@Preview(
    device = "spec:parent=pixel_5,orientation=landscape",
    showBackground = true,
    backgroundColor = 0xFFE6E6FF,
)
@Composable
private fun ScreenPreviewLandscape() {
    val backStack = remember { mutableStateListOf<NavigationRoute>(DeckRoute(1)) }
    val navigator = remember { StackNavigator(backStack) }
    val decksState: UiState<List<Deck>> = decksDataMock()
    val createModalWindow = false
    val dropdownMenuState = DropdownMenuState()

    MindeckTheme {
        DecksContent(
            navigator,
            decksState,
            UiState.Loading,
            dropdownMenuState,
            createModalWindow,
            { },
            { _ -> },
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFE6E6FF,
)
@Composable
private fun RenameDeckModalWindowScreenPreview() {
    val backStack = remember { mutableStateListOf<NavigationRoute>(DeckRoute(1)) }
    val navigator = remember { StackNavigator(backStack) }
    val decksState: UiState<List<Deck>> = decksDataMock()

    val createModalWindow = true
    val dropdownMenuState = DropdownMenuState()
    MindeckTheme {
        DecksContent(
            navigator,
            decksState,
            UiState.Loading,
            dropdownMenuState,
            createModalWindow,
            { },
            { _ -> },
        )
    }
}

@Preview(
    device = "spec:parent=pixel_5,orientation=landscape",
    showBackground = true,
    backgroundColor = 0xFFE6E6FF,
)
@Composable
private fun EditElementModalWindowScreenPreview() {
    val backStack = remember { mutableStateListOf<NavigationRoute>(DeckRoute(1)) }
    val navigator = remember { StackNavigator(backStack) }
    val decksState: UiState<List<Deck>> = decksDataMock()
    val createModalWindow = true
    val dropdownMenuState = DropdownMenuState()

    MindeckTheme {
        DecksContent(
            navigator,
            decksState,
            UiState.Loading,
            dropdownMenuState,
            createModalWindow,
            { },
            { _ -> },
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
