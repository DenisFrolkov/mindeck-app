package com.mindeck.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.common.DisplayItemCount
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDropdownMenuHeightIn
import com.mindeck.presentation.ui.components.folder.DisplayCardFolder
import com.mindeck.presentation.ui.components.folder.FolderData
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.BackgroundScreen
import com.mindeck.presentation.ui.theme.Black
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.LightBlue
import com.mindeck.presentation.uiState.UiState
import com.mindeck.presentation.viewmodel.FoldersViewModel

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun FoldersScreen(navController: NavController, foldersViewModel: FoldersViewModel) {

    var dropdownMenuState = remember { DropdownMenuState() }

    val dropdownVisibleAnimation = animateDropdownMenuHeightIn(
        targetAlpha = dropdownMenuState.dropdownAlpha,
        animationDuration = dropdownMenuState.animationDuration
    )

    var fontFamily = remember { FontFamily(Font(R.font.opensans_medium)) }
    var textStyle = remember { TextStyle(fontSize = 14.sp, color = Black, fontFamily = fontFamily) }

    val folders = foldersViewModel.folderUIState.collectAsState().value

    var listDropdownMenu = listOf(
        DropdownMenuData(
            title = "Изменить название",
            action = {
            }
        ),
        DropdownMenuData(
            title = "Удалить элемент",
            action = {
            }
        ),
        DropdownMenuData(
            title = "Добавить элемент",
            action = {
            }
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundScreen)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .statusBarsPadding(),
        containerColor = BackgroundScreen,
        topBar = {
            ActionBar(
                onBackClick = { navController.popBackStack() },
                onMenuClick = { dropdownMenuState.toggle() },
                containerModifier = Modifier
                    .fillMaxWidth(),
                iconModifier = Modifier
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(color = Blue, shape = RoundedCornerShape(50.dp))
                    .padding(all = 12.dp)
                    .size(size = 16.dp),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
            ) {
                when (folders) {
                    is UiState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is UiState.Success -> {
                        DisplayItemCount(
                            pluralsTextOne = R.plurals.folder_amount,
                            pluralsTextTwo = R.plurals.deck_amount,
                            listOne = folders.data,
                            listTwo = listOf(1),
                            textStyle = textStyle
                        )
                        LazyColumn {
                            items(
                                items = folders.data,
                                key = { it.folderId }) {
                                DisplayCardFolder(
                                    folderIcon = painterResource(R.drawable.folder_icon),
                                    numberOfCards = 12,
                                    folderName = it.folderName,
                                    backgroundColor = Blue,
                                    iconColor = LightBlue,
                                    onClick = { navController.navigate(NavigationRoute.FolderScreen.route) },
                                    textStyle = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.opensans_medium))
                                    ),
                                    modifier = Modifier
                                )
                                Spacer (modifier = Modifier.height(6.dp))
                            }
                        }
                    }

                    is UiState.Error -> {
                        Text("Error: $folders")
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
            }
            if (dropdownMenuState.isExpanded) {
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
}
