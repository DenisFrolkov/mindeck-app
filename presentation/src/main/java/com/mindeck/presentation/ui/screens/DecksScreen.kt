package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.border
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.RenderUiState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemData
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemStyle
import com.mindeck.presentation.ui.components.dialog.CustomModalWindow
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.folder.DisplayItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.viewmodel.DecksViewModel

@Composable
fun DecksScreen(
    navController: NavController
) {
    val decksViewModel: DecksViewModel = hiltViewModel(navController.currentBackStackEntry!!)

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
        navController,
        decks,
        createDeckState,
        dropdownMenuState,
        modalWindowValue,
        onSaveDeck = { deckName ->
            decksViewModel.createDeck(deckName)
        },
        toggleCreateDeckModalWindow = {
            decksViewModel.toggleModalWindow(it)
        }
    )
}

@Composable
private fun DecksContent(
    navController: NavController,
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
            DecksEditTopBar(navController, dropdownMenuState)
        },
        content = { padding ->
            PageContent(padding, decks, navController, listDropdownMenu, dropdownMenuState)

            if (initialDialog) {
                Dialog(
                    onDismissRequest = {
                        toggleCreateDeckModalWindow(false)
                    }
                ) {
                    CustomModalWindow(
                        stringResource(R.string.create_item_dialog_text_creating_deck),
                        stringResource(R.string.create_item_dialog_text_create_deck),
                        stringResource(R.string.create_item_dialog_text_input_name_deck),
                        createDeckState,
                        exitButton = {
                            toggleCreateDeckModalWindow(false)
                        },
                        saveButton = {
                            onSaveDeck(it)
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun dropdownMenuDataList(
    onCreateDeck: () -> Unit
): List<DropdownMenuData> {
    return listOf(
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_create_deck_list),
            titleStyle = MaterialTheme.typography.bodyMedium,
            action = {
                onCreateDeck()
            }
        )
    )
}

@Composable
private fun DecksEditTopBar(
    navController: NavController,
    dropdownMenuState: DropdownMenuState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionHandlerButton(
            iconPainter = painterResource(R.drawable.back_icon),
            contentDescription = stringResource(R.string.back_screen_icon_button),
            iconTint = MaterialTheme.colorScheme.onPrimary,
            onClick = { navController.popBackStack() },
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
private fun PageContent(
    padding: PaddingValues,
    decks: UiState<List<Deck>>,
    navController: NavController,
    listDropdownMenu: List<DropdownMenuData>,
    dropdownMenuState: DropdownMenuState
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
    ) {
        DecksInfo(decks, navController)
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
    navController: NavController
) {
    decksState.RenderUiState(
        onSuccess = { decks ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(
                    text = pluralStringResource(
                        R.plurals.deck_amount,
                        decks.size,
                        decks.size
                    ),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_large)))
            LazyColumn {
                items(
                    items = decks,
                    key = { it.deckId }) { deck ->
                    DisplayItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                dimenDpResource(R.dimen.border_width_dot_two_five),
                                MaterialTheme.colorScheme.outline,
                                MaterialTheme.shapes.medium
                            )
                            .clip(MaterialTheme.shapes.medium)
                            .height(dimenDpResource(R.dimen.display_card_item_size))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                navController.navigate(
                                    NavigationRoute.DeckScreen.createRoute(
                                        deck.deckId
                                    )
                                )
                            },
                        showCount = false,
                        displayItemData = DisplayItemData(
                            itemIcon = R.drawable.deck_icon,
                            numberOfCards = deck.deckId,
                            itemName = deck.deckName,
                        ),
                        displayItemStyle = DisplayItemStyle(
                            backgroundColor = MaterialTheme.colorScheme.secondary.copy(
                                dimenFloatResource(R.dimen.float_zero_dot_five_significance)
                            ),
                            iconColor = MaterialTheme.colorScheme.outlineVariant,
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
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
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_one)
                )
            }
        },
        onError = {
            Text(
                stringResource(R.string.error_get_all_decks),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFE6E6FF
)
@Composable
private fun ScreenPreview() {
    val navController = rememberNavController()
    val decksState: UiState<List<Deck>> = decksDataMock()
    val dropdownMenuState = remember { DropdownMenuState() }

    MindeckTheme {
        DecksContent(
            navController,
            decksState,
            UiState.Loading,
            dropdownMenuState,
            false,
            { },
            { _ -> }
        )
    }
}

@Composable
private fun DeckDropdownMenu(
    padding: PaddingValues,
    listDropdownMenu: List<DropdownMenuData>,
    dropdownMenuState: DropdownMenuState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { dropdownMenuState.toggle() })

    DropdownMenu(
        listDropdownMenuItem = listDropdownMenu,
        dropdownModifier = Modifier
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(padding)
            .fillMaxWidth()
            .padding(top = dimenDpResource(R.dimen.spacer_extra_small))
            .wrapContentSize(Alignment.TopEnd)
    )
}

//@Preview(
//    device = "spec:parent=pixel_5,orientation=landscape",
//    showBackground = true,
//    backgroundColor = 0xFFE6E6FF
//)
//@Composable
//private fun ScreenPreviewLandscape() {
//    val navController = rememberNavController()
//    val decksState: UiState<List<Deck>> = decksDataMock()
//    val cardsForRepetitionState: UiState<List<Card>> = cardsForRepetitionDataMock()
//
//    MindeckTheme {
//        MainContent(
//            navController,
//            decksState,
//            cardsForRepetitionState,
//        )
//    }
//}
//
//@Preview(
//    showBackground = true,
//    backgroundColor = 0xFFE6E6FF
//)
//@Composable
//private fun ScreenPreviewOpenFAB() {
//    val navController = rememberNavController()
//    val decksState: UiState<List<Deck>> = decksDataMock()
//    val cardsForRepetitionState: UiState<List<Card>> = cardsForRepetitionDataMock()
//    val fabModalWindow = true
//
//    MindeckTheme {
//        MainContent(
//            navController,
//            decksState,
//            cardsForRepetitionState,
//            fabModalWindow
//        )
//    }
//}
//
//@Preview(
//    device = "spec:parent=pixel_5,orientation=landscape",
//    showBackground = true,
//    backgroundColor = 0xFFE6E6FF
//)
//@Composable
//private fun ScreenPreviewOpenFABLandscape() {
//    val navController = rememberNavController()
//    val decksState: UiState<List<Deck>> = decksDataMock()
//    val cardsForRepetitionState: UiState<List<Card>> = cardsForRepetitionDataMock()
//
//    MindeckTheme {
//        DecksContent(
//            navController,
//            decksState,
//            cardsForRepetitionState
//        )
//    }
//}

@Composable
private fun decksDataMock(): UiState<List<Deck>> = UiState.Success(
    listOf<Deck>(
        Deck(
            deckId = 1,
            deckName = "Kotlin Basics"
        ),
        Deck(
            deckId = 2,
            deckName = "Jetpack Compose"
        ),
        Deck(
            deckId = 3,
            deckName = "Architecture Patterns"
        ),
        Deck(
            deckId = 4,
            deckName = "Coroutines & Flow"
        )
    )
)