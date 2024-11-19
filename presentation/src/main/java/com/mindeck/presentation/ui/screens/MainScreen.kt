package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.GetAllFindersButton
import com.mindeck.presentation.ui.components.daily_progress_tracker.DailyProgressTracker
import com.mindeck.presentation.ui.components.fab.FAB
import com.mindeck.presentation.ui.components.fab.FabMenuDataClass
import com.mindeck.presentation.ui.components.fab.FabState
import com.mindeck.presentation.ui.components.fab.FabState.Companion.ITEM_HEIGHT
import com.mindeck.presentation.ui.components.folder.DisplayCardFolder
import com.mindeck.presentation.ui.components.folder.FolderData
import com.mindeck.presentation.ui.theme.Black
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.LightBlue
import com.mindeck.presentation.ui.theme.LightMint
import com.mindeck.presentation.ui.theme.LimeGreen
import com.mindeck.presentation.ui.theme.White

@Composable
fun MainScreen() {

    val textStyle = TextStyle(
        fontSize = 14.sp,
        color = White,
        fontFamily = FontFamily(Font(R.font.opensans_medium))
    )

    val fabMenuItems = listOf(
        FabMenuDataClass(
            idItem = 0,
            text = "Настройки",
            icon = painterResource(R.drawable.fab_open_menu_setting_icon)
        ),
        FabMenuDataClass(
            idItem = 1, text = "Поиск", icon = painterResource(R.drawable.search_icon)
        ),
        FabMenuDataClass(
            idItem = 2,
            text = "Создать картчоку",
            icon = painterResource(R.drawable.fab_open_menu_create_icon)
        )
    )

    val folders = listOf(
        FolderData(
            789,
            "Повторите, чтобы не забыть!",
            LightMint,
            LimeGreen,
            R.drawable.repeat_card_item
        ),
        FolderData(123, "Общая папка", LightBlue, Blue, R.drawable.folder_icon),
        FolderData(152, "Папка №1", LightBlue, Blue, R.drawable.folder_icon),
        FolderData(256, "Папка №2", LightBlue, Blue, R.drawable.folder_icon)
    )

    val fabState = remember { FabState(expandedHeight = with(LocalDensity) { ITEM_HEIGHT.dp * fabMenuItems.size } ) }

    LazyColumn(
        modifier =
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(start = 16.dp, top = 20.dp, end = 16.dp)
    ) {
        item {
            DailyProgressTracker(dptIcon = painterResource(R.drawable.dpt_icon))
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(folders) {
            DisplayCardFolder(
                folderIcon =
                painterResource(it.icon),
                numberOfCards = it.countCard,
                folderName = it.text,
                backgroundColor = it.color,
                iconColor = it.colorTwo,
                onClick = { },
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.opensans_medium))
                ),
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
        item {
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
                        ) { },
                    textModifier = Modifier.padding(vertical = 8.dp, horizontal = 33.dp)
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                fabState.reset()
            }
            .background(if (fabState.isExpanded) Black.copy(alpha = 0.3f) else Color.Transparent)
            .navigationBarsPadding()
            .padding(16.dp)
            .wrapContentSize(Alignment.BottomEnd)
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