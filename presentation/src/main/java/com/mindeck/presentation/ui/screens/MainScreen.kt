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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.GetAllFindersButton
import com.mindeck.presentation.ui.components.daily_progress_tracker.DailyProgressTracker
import com.mindeck.presentation.ui.components.daily_progress_tracker.DailyProgressTrackerState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.fab.FAB
import com.mindeck.presentation.ui.components.fab.FabMenuData
import com.mindeck.presentation.ui.components.fab.FabState
import com.mindeck.presentation.ui.components.fab.FabState.Companion.ITEM_HEIGHT
import com.mindeck.presentation.ui.components.fab.animateScreenAlpha
import com.mindeck.presentation.ui.components.folder.DisplayCardItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
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

    val dailyProgressTrackerState = remember {
        DailyProgressTrackerState(
            totalCards = 500,
            answeredCards = 30
        )
    }

    val MAX_DISPLAY_ITEMS = 5

    val fabMenuItems = listOf(
        FabMenuData(
            idItem = 0,
            text = stringResource(R.string.fab_menu_data_setting_list),
            icon = R.drawable.fab_open_menu_setting_icon,
            navigation = { }
        ),
        FabMenuData(
            idItem = 1,
            text = stringResource(R.string.fab_menu_data_create_card_list),
            icon = R.drawable.fab_open_menu_create_icon,
            navigation = { navController.navigate(NavigationRoute.CreationCardScreen.route) }
        )
    )


    val folders = mainViewModel.folderUIState.collectAsState().value
    val fabState = remember { FabState(expandedHeight = ITEM_HEIGHT.dp * fabMenuItems.size) }
    val alphaScreen = animateScreenAlpha(
        targetAlpha = fabState.screenAlphaValue,
        animationDuration = fabState.animationDuration
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(state = scrollState)
            .statusBarsPadding()
            .padding(all = dimenDpResource(R.dimen.padding_medium))
    ) {
        DailyProgressTracker(
            dptIcon = painterResource(R.drawable.dpt_icon),
            dailyProgressTrackerState = dailyProgressTrackerState
        )

        Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_large)))

        when (folders) {
            is UiState.Success -> {
                folders.data.take(MAX_DISPLAY_ITEMS).forEach { folder ->
                    DisplayCardItem(
                        showCount = true,
                        itemIcon =
                        painterResource(R.drawable.folder_icon),
                        numberOfCards = folder.folderId,
                        itemName = folder.folderName,
                        backgroundColor = MaterialTheme.colorScheme.outlineVariant,
                        iconColor = MaterialTheme.colorScheme.secondary,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                dimenDpResource(R.dimen.border_width),
                                MaterialTheme.colorScheme.outline,
                                MaterialTheme.shapes.small
                            )
                            .clip(shape = MaterialTheme.shapes.small)
                            .height(dimenDpResource(R.dimen.display_card_item_size))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                navController.navigate(
                                    NavigationRoute.FolderScreen.createRoute(
                                        folder.folderId
                                    )
                                )
                            }

                    )

                    Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_small)))
                }
                if (folders.data.size > MAX_DISPLAY_ITEMS) {

                    Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_small)))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        GetAllFindersButton(
                            buttonText = stringResource(R.string.title_text_all_folders),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    navController.navigate(NavigationRoute.FoldersScreen.route)
                                },
                            textModifier = Modifier.padding(
                                vertical = dimenDpResource(R.dimen.padding_medium),
                                horizontal = dimenDpResource(R.dimen.padding_extra_large)
                            )
                        )
                    }
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
                .background(
                    if (fabState.isExpanded) MaterialTheme.colorScheme.scrim.copy(
                        alphaScreen
                    ) else Color.Transparent
                ))
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(dimenDpResource(R.dimen.padding_medium))
            .wrapContentSize(Alignment.BottomEnd)
            .onGloballyPositioned {
                val offset = it.localToWindow(Offset.Zero)
                onButtonPositioned(IntOffset(offset.x.roundToInt(), offset.y.roundToInt()))
            }
    ) {
        FAB(
            fabColor = MaterialTheme.colorScheme.outlineVariant,
            fabIconColor = MaterialTheme.colorScheme.onPrimary,
            fabIcon = painterResource(R.drawable.fab_menu_icon),
            fabMenuItems = fabMenuItems,
            fabState = fabState,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}