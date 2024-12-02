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
import androidx.compose.runtime.Composable
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
import com.mindeck.presentation.ui.components.fab.FabMenuDataClass
import com.mindeck.presentation.ui.components.fab.FabState
import com.mindeck.presentation.ui.components.fab.FabState.Companion.ITEM_HEIGHT
import com.mindeck.presentation.ui.components.fab.animateScreenAlpha
import com.mindeck.presentation.ui.components.folder.DisplayCardFolder
import com.mindeck.presentation.ui.components.folder.FolderData
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.BackgroundScreen
import com.mindeck.presentation.ui.theme.Black
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.LightBlue
import com.mindeck.presentation.ui.theme.LightMint
import com.mindeck.presentation.ui.theme.LimeGreen
import com.mindeck.presentation.ui.theme.White
import kotlin.math.roundToInt

@Composable
fun MainScreen(
    navController: NavController,
    onButtonPositioned: (IntOffset) -> Unit
) {
    val scrollState = rememberScrollState()

    val textStyle = remember {
        TextStyle(
            fontSize = 14.sp,
            color = White,
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
            FabMenuDataClass(
                idItem = 0,
                text = "Настройки",
                icon = R.drawable.fab_open_menu_setting_icon,
                navigation = { navController.navigate(NavigationRoute.CreationCardScreen.route) }
            ),
            FabMenuDataClass(
                idItem = 1,
                text = "Создать картчоку",
                icon = R.drawable.fab_open_menu_create_icon,
                navigation = { navController.navigate(NavigationRoute.CreationCardScreen.route) }
            )
        )
    }

    val folders =
        listOf(
            FolderData(
                0,
                789,
                "Повторите, чтобы не забыть!",
                LightMint,
                LimeGreen,
                R.drawable.repeat_card_item
            ),
            FolderData(1, 123, "Общая папка", LightBlue, Blue, R.drawable.folder_icon),
            FolderData(2, 152, "Папка №1", LightBlue, Blue, R.drawable.folder_icon),
            FolderData(3, 256, "Папка №2", LightBlue, Blue, R.drawable.folder_icon),
            FolderData(4, 256, "Папка №2", LightBlue, Blue, R.drawable.folder_icon),
            FolderData(5, 256, "Папка №2", LightBlue, Blue, R.drawable.folder_icon),
        )

    val fabState =
        remember { FabState(expandedHeight = ITEM_HEIGHT.dp * fabMenuItems.size) }

    val alphaScreen = animateScreenAlpha(fabState.screenAlphaValue, fabState.animationDuration)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundScreen)
            .verticalScroll(state = scrollState)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        DailyProgressTracker(dptIcon = painterResource(R.drawable.dpt_icon))
        Spacer(modifier = Modifier.height(20.dp))
        folders.take(5).forEach {
                DisplayCardFolder(
                    folderIcon =
                    painterResource(it.icon),
                    numberOfCards = it.countCard,
                    folderName = it.text,
                    backgroundColor = it.color,
                    iconColor = it.colorTwo,
                    onClick = {
                        navController.navigate(NavigationRoute.FolderScreen.route)
                    },
                    textStyle = textStyleDisplayCard,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        if (folders.size > 5) {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ) {
                GetAllFindersButton(
                    textStyle = textStyle,
                    modifier = Modifier
                        .background(color = Blue, shape = RoundedCornerShape(12.dp))
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
    if (fabState.isExpanded) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                fabState.reset()
            }
            .background(if (fabState.isExpanded) Black.copy(alphaScreen) else Color.Transparent))
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
            fabColor = Blue,
            fabIconColor = White,
            fabIcon = painterResource(R.drawable.fab_menu_icon),
            fabMenuItems = fabMenuItems,
            fabState = fabState,
            textStyle = textStyle
        )
    }
}