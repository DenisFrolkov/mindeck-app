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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import com.mindeck.presentation.ui.components.buttons.GetAllFindersButton
import com.mindeck.presentation.ui.components.daily_progress_tracker.DailyProgressTracker
import com.mindeck.presentation.ui.components.fab.FAB
import com.mindeck.presentation.ui.components.fab.FabMenuData
import com.mindeck.presentation.ui.components.fab.FabState
import com.mindeck.presentation.ui.components.fab.FabState.Companion.ITEM_HEIGHT
import com.mindeck.presentation.ui.components.fab.animateScreenAlpha
import com.mindeck.presentation.ui.components.folder.DisplayCardFolder
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.background_light_blue
import com.mindeck.presentation.ui.theme.scrim_black
import com.mindeck.presentation.ui.theme.outline_variant_blue
import com.mindeck.presentation.ui.theme.repeat_button_light_blue
import com.mindeck.presentation.ui.theme.on_primary_white
import com.mindeck.presentation.uiState.UiState
import com.mindeck.presentation.viewmodel.MainViewModel
import kotlin.math.roundToInt

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    onButtonPositioned: (IntOffset) -> Unit
) {
    val scrollState = rememberScrollState()

    val textStyle = remember {
        TextStyle(
            fontSize = 14.sp,
            color = on_primary_white,
            fontFamily = FontFamily(Font(R.font.opensans_medium))
        )
    }

    val textStyleDisplayCard = remember {
        TextStyle(
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.opensans_medium))
        )
    }

    val fabMenuItems = remember {
        listOf(
            FabMenuData(
                idItem = 0,
                text = "Настройки",
                icon = R.drawable.fab_open_menu_setting_icon,
                navigation = { navController.navigate(NavigationRoute.CreationCardScreen.route) }
            ),
            FabMenuData(
                idItem = 1,
                text = "Создать картчоку",
                icon = R.drawable.fab_open_menu_create_icon,
                navigation = { navController.navigate(NavigationRoute.CreationCardScreen.route) }
            )
        )
    }

    val folders = mainViewModel.folderUIState.collectAsState().value

    val fabState =
        remember { FabState(expandedHeight = ITEM_HEIGHT.dp * fabMenuItems.size) }

    val alphaScreen = animateScreenAlpha(fabState.screenAlphaValue, fabState.animationDuration)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background_light_blue)
            .verticalScroll(state = scrollState)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        DailyProgressTracker(dptIcon = painterResource(R.drawable.dpt_icon))
        Spacer(modifier = Modifier.height(20.dp))
        when (folders) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Success -> {
                folders.data.take(5).forEach {
                    DisplayCardFolder(
                        folderIcon =
                        painterResource(R.drawable.folder_icon),
                        numberOfCards = it.folderId,
                        folderName = it.folderName,
                        backgroundColor = outline_variant_blue,
                        iconColor = repeat_button_light_blue,
                        onClick = {
                            navController.navigate(NavigationRoute.FolderScreen.createRoute(it.folderId))
                        },
                        textStyle = textStyleDisplayCard,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
                if (folders.data.size > 5) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        GetAllFindersButton(
                            textStyle = textStyle,
                            modifier = Modifier
                                .background(color = outline_variant_blue, shape = RoundedCornerShape(12.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    navController.navigate(NavigationRoute.FoldersScreen.route)
                                },
                            textModifier = Modifier.padding(vertical = 8.dp, horizontal = 33.dp)
                        )
                    }
                }
            }

            is UiState.Error -> {
                Text("Error: $folders")
            }
        }
        if (fabState.isExpanded) {
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    fabState.reset()
                }
                .background(if (fabState.isExpanded) scrim_black.copy(alphaScreen) else Color.Transparent))
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(16.dp)
            .wrapContentSize(Alignment.BottomEnd)
            .onGloballyPositioned {
                val offset = it.localToWindow(Offset.Zero)
                onButtonPositioned(IntOffset(offset.x.roundToInt(), offset.y.roundToInt()))
            }
    ) {
        FAB(
            fabColor = outline_variant_blue,
            fabIconColor = on_primary_white,
            fabIcon = painterResource(R.drawable.fab_menu_icon),
            fabMenuItems = fabMenuItems,
            fabState = fabState,
            textStyle = textStyle
        )
    }
}