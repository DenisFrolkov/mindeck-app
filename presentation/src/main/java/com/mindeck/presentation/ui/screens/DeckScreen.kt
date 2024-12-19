package com.mindeck.presentation.ui.screens

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
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
import com.mindeck.presentation.ui.components.folder.DisplayCardItem
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.background_light_blue
import com.mindeck.presentation.ui.theme.scrim_black
import com.mindeck.presentation.ui.theme.outline_variant_blue
import com.mindeck.presentation.ui.theme.repeat_button_light_blue
import com.mindeck.presentation.uiState.UiState
import com.mindeck.presentation.viewmodel.DeckViewModel
import kotlin.math.roundToInt

@Composable
fun DeckScreen(
    navController: NavController,
    onButtonPositioned: (IntOffset) -> Unit,
    deckViewModel: DeckViewModel
) {

    var dropdownMenuState = remember { DropdownMenuState() }

    val dropdownVisibleAnimation = animateDropdownMenuHeightIn(
        targetAlpha = dropdownMenuState.dropdownAlpha,
        animationDuration = dropdownMenuState.animationDuration
    )
    var fontFamily = FontFamily(Font(R.font.opensans_medium))
    var textStyle = TextStyle(fontSize = 14.sp, color = scrim_black, fontFamily = fontFamily)

    val cards = deckViewModel.cardUIState.collectAsState().value

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
            title = "Добавить карточку",
            action = {

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
                when (cards) {
                    is UiState.Success -> {
                        Text(
                            text = "Название колоды",
                            style = textStyle,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                        )
                        Spacer(Modifier.height(18.dp))
                        DisplayItemCount(
                            pluralsTextOne = R.plurals.card_amount,
                            listOne = cards.data,
                            textStyle = textStyle
                        )

                        LazyColumn {
                            items(items = cards.data, key = { it.deckId }) {
                                DisplayCardItem(
                                    cardIcon =
                                    painterResource(R.drawable.card_icon),
                                    titleCard = it.cardName,
                                    backgroundColor = repeat_button_light_blue,
                                    iconColor = outline_variant_blue,
                                    onClick = {
                                        navController.navigate(NavigationRoute.CreationCardScreen.route)
                                    },
                                    textStyle = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.opensans_medium))
                                    ),
                                    modifier = Modifier
                                        .onGloballyPositioned {
                                            val offset = it.localToWindow(Offset.Zero)
                                            onButtonPositioned(
                                                IntOffset(
                                                    offset.x.roundToInt(),
                                                    offset.y.roundToInt()
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