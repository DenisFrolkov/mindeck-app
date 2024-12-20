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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.Folder
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.CreateItemDialog
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.common.DisplayItemCount
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDialogCreateItem
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDropdownMenuHeightIn
import com.mindeck.presentation.ui.components.folder.DisplayCardItem
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.background_light_blue
import com.mindeck.presentation.ui.theme.scrim_black
import com.mindeck.presentation.ui.theme.outline_variant_blue
import com.mindeck.presentation.ui.theme.outline_medium_gray
import com.mindeck.presentation.ui.theme.secondary_light_blue
import com.mindeck.presentation.uiState.UiState
import com.mindeck.presentation.viewmodel.DeckViewModel
import com.mindeck.presentation.viewmodel.FolderViewModel

@Composable
fun FolderScreen(
    navController: NavController,
    folderViewModel: FolderViewModel,
    deckViewModel: DeckViewModel
) {

    var dropdownMenuState = remember { DropdownMenuState() }

    var fontFamily = FontFamily(Font(R.font.opensans_medium))
    var textStyle = TextStyle(fontSize = 14.sp, color = scrim_black, fontFamily = fontFamily)

    val dropdownVisibleAnimation = animateDropdownMenuHeightIn(
        targetAlpha = dropdownMenuState.dropdownAlpha,
        animationDuration = dropdownMenuState.animationDuration
    )

    val dialogVisibleAnimation = animateDialogCreateItem(
        targetAlpha = dropdownMenuState.dialogAlpha,
        animationDuration = 300
    )

    val folder = folderViewModel.folderUIState.collectAsState().value
    val decks = folderViewModel.deckByIdrUIState.collectAsState().value

    val deleteFolderData =
        remember { mutableStateOf(Folder(folderId = 0, folderName = "")) }

    val folderId = remember { mutableIntStateOf(0) }

    var createDeck by remember { mutableStateOf("") }

    var listDropdownMenu = listOf(
        DropdownMenuData(
            title = "Изменить название",
            action = {
//                folderViewModel.renameFolder()
            }
        ),
        DropdownMenuData(
            title = "Удалить элемент",
            action = {
                folderViewModel.deleteFolder(deleteFolderData.value)
                navController.popBackStack()
            }
        ),
        DropdownMenuData(
            title = "Создать колоду",
            action = {
                dropdownMenuState.openDialog()
            }
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background_light_blue)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .statusBarsPadding(),
        containerColor = background_light_blue,
        topBar = {
            ActionBar(
                onBackClick = { navController.popBackStack() },
                onMenuClick = { dropdownMenuState.toggle() },
                containerModifier = Modifier
                    .fillMaxWidth(),
                iconModifier = Modifier
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(color = outline_variant_blue, shape = RoundedCornerShape(50.dp))
                    .padding(all = 12.dp)
                    .size(size = 16.dp),
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                when (folder) {
                    is UiState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is UiState.Success -> {
                        deleteFolderData.value = folder.data
                        folderId.intValue = folder.data.folderId
                        Text(
                            text = folder.data.folderName,
                            style = textStyle,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                        )
                        Spacer(Modifier.height(18.dp))
                        when (decks) {
                            is UiState.Success -> {
                                DisplayItemCount(
                                    pluralsTextOne = R.plurals.deck_amount,
                                    listOne = decks.data,
                                    textStyle = textStyle
                                )
                            }
                        }
                    }
                }
                when (decks) {
                    is UiState.Success -> {
                        LazyColumn(modifier = Modifier) {
                            items(items = decks.data, key = { it.deckId }) {
                                DisplayCardItem(
                                    showCount = true,
                                    itemIcon =
                                    painterResource(R.drawable.deck_icon),
                                    numberOfCards = it.deckId,
                                    itemName = it.deckName,
                                    backgroundColor = secondary_light_blue.copy(.5f),
                                    iconColor = outline_variant_blue,
                                    textStyle = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.opensans_medium))
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(0.25.dp, outline_medium_gray, RoundedCornerShape(4.dp))
                                        .clip(shape = RoundedCornerShape(4.dp))
                                        .height(48.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            navController.navigate(
                                                NavigationRoute.DeckScreen.createRoute(
                                                    it.deckId
                                                )
                                            )
                                        }
                                )
                                Spacer(modifier = Modifier.height(6.dp))
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
                    textStyle = textStyle,
                    dropdownModifier = Modifier
                        .padding(padding)
                        .alpha(dropdownVisibleAnimation)
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .wrapContentSize(Alignment.TopEnd)
                )
            }
        }
    )

    if (dropdownMenuState.isOpeningDialog) {
        Box(modifier = Modifier.alpha(dialogVisibleAnimation)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(outline_medium_gray.copy(0.5f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {

                    }
            )
            CreateItemDialog(
                titleDialog = "Создание колоды",
                placeholder = "Введите название колоды",
                buttonText = "Создать колоду",
                value = createDeck,
                onValueChange = { newValue -> createDeck = newValue },
                onBackClick = {
                    dropdownMenuState.closeDialog()
                },
                onClickButton = {
                    folderViewModel.createDeck(Deck(deckName = createDeck, folderId = folderId.intValue))
                    dropdownMenuState.closeDialog()
                },
                fontFamily = FontFamily(Font(R.font.opensans_medium)),
                titleTextStyle = textStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.CenterStart),
                iconModifier = Modifier
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(color = outline_variant_blue, shape = RoundedCornerShape(50.dp))
                    .padding(all = 12.dp)
                    .size(size = 16.dp),
                buttonModifier = Modifier
            )
        }
    }
}
