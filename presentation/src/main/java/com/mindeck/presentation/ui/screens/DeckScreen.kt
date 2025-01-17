package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.common.DisplayItemCount
import com.mindeck.presentation.ui.components.dialog.CreateItemDialog
import com.mindeck.presentation.ui.components.dialog.DialogState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDialogCreateItem
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDropdownMenuHeightIn
import com.mindeck.presentation.ui.components.folder.DisplayCardItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.uiState.UiState
import com.mindeck.presentation.viewmodel.DeckViewModel

@Composable
fun DeckScreen(
    navController: NavController,
    deckViewModel: DeckViewModel,
) {

    var dropdownMenuState = remember { DropdownMenuState() }

    var dialogState = remember { DialogState() }

    val dropdownVisibleAnimation = animateDropdownMenuHeightIn(
        targetAlpha = dropdownMenuState.dropdownAlpha,
        animationDuration = dropdownMenuState.animationDuration
    )

    val dialogVisibleAnimation = animateDialogCreateItem(
        targetAlpha = dialogState.dialogAlpha,
        animationDuration = dialogState.animationDuration * 3
    )

    val cards = deckViewModel.cardUIState.collectAsState().value
    val deck = deckViewModel.deckUIState.collectAsState().value

    val deleteDeckData =
        remember { mutableStateOf(Deck(deckId = 0, deckName = "", folderId = 0)) }

    var listDropdownMenu = listOf(
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_rename_list),
            action = {
                dialogState.openRenameDialog()
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_create_card),
            action = {
                navController.navigate(NavigationRoute.CreationCardScreen.route)
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_remote_list),
            action = {
                deckViewModel.deleteDeck(deleteDeckData.value)
            }
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ActionBar(
                onBackClick = { navController.popBackStack() },
                onMenuClick = { dropdownMenuState.toggle() },
                containerModifier = Modifier
                    .fillMaxWidth(),
                iconModifier = Modifier
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .background(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(dimenDpResource(R.dimen.padding_small))
                    .size(dimenDpResource(R.dimen.padding_medium)),
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                when (deck) {
                    is UiState.Success -> {
                        deleteDeckData.value = deck.data
                        Text(
                            text = deck.data.deckName,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }
                Spacer(Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
                when (cards) {
                    is UiState.Success -> {
                        DisplayItemCount(
                            plurals = R.plurals.card_amount,
                            count = cards.data.size,
                            textStyle = MaterialTheme.typography.bodyMedium
                        )

                        LazyColumn {
                            items(items = cards.data, key = { it.cardId }) { card ->
                                DisplayCardItem(
                                    showCount = false,
                                    itemIcon = painterResource(R.drawable.card_icon),
                                    itemName = card.cardName,
                                    backgroundColor = MaterialTheme.colorScheme.secondary.copy(
                                        dimenFloatResource(R.dimen.float_zero_dot_five_significance)
                                    ),
                                    iconColor = MaterialTheme.colorScheme.outlineVariant,
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            dimenDpResource(R.dimen.border_width_dot_two_five),
                                            MaterialTheme.colorScheme.outline,
                                            MaterialTheme.shapes.extraSmall
                                        )
                                        .clip(shape = MaterialTheme.shapes.extraSmall)
                                        .height(dimenDpResource(R.dimen.display_card_item_size))
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            navController.navigate(NavigationRoute.CardScreen.route)
                                        }
                                )
                                Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_small)))
                            }
                        }
                    }
                }
            }
            if (dropdownMenuState.isExpanded) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { dropdownMenuState.toggle() })

                DropdownMenu(
                    listDropdownMenuItem = listDropdownMenu,
                    dropdownModifier = Modifier
                        .padding(padding)
                        .alpha(dropdownVisibleAnimation)
                        .fillMaxWidth()
                        .padding(top = dimenDpResource(R.dimen.spacer_extra_small))
                        .wrapContentSize(Alignment.TopEnd)
                )
            }
        }
    )

    if (dialogState.isOpeningDialog) {
        Box(modifier = Modifier.alpha(dialogVisibleAnimation)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.outline.copy(dimenFloatResource(R.dimen.float_zero_dot_five_significance)))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {}
            )
            when (deck) {
                is UiState.Success -> {
                    CreateItemDialog(
                        titleDialog = stringResource(R.string.rename_title_item_dialog),
                        placeholder = stringResource(R.string.rename_item_dialog_text_input_title_deck),
                        buttonText = stringResource(R.string.save_text),
                        value = dialogState.isEnterDialogText,
                        validation = true,
                        onValueChange = { newValue -> dialogState.isEnterDialogText = newValue },
                        onBackClick = {
                            dialogState.closeDialog()
                        },
                        onClickButton = {
                            deckViewModel.renameDeck(
                                deckId = deck.data.deckId,
                                newDeckName = dialogState.isEnterDialogText
                            )
                            dialogState.closeDialog()
                            deckViewModel.getDeckById(deck.data.deckId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.CenterStart),
                        iconModifier = Modifier
                            .clip(shape = MaterialTheme.shapes.extraLarge)
                            .background(
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                            .padding(dimenDpResource(R.dimen.padding_small))
                            .size(dimenDpResource(R.dimen.padding_medium)),
                    )
                }
            }
        }
    }
}