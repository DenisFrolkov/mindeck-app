package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mindeck.presentation.ui.components.folder.DisplayCardFolder
import com.mindeck.presentation.ui.components.folder.FolderData
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.BackgroundScreen
import com.mindeck.presentation.ui.theme.Black
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.LightBlue

@Composable
fun FoldersScreen(navController: NavController) {
    var fontFamily = remember { FontFamily(Font(R.font.opensans_medium)) }
    var textStyle = remember { TextStyle(fontSize = 14.sp, color = Black, fontFamily = fontFamily) }

    val folders = listOf(
        FolderData(0, 123, "Общая колода", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(1, 152, "Колода номер 1", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(2, 152, "Колода номер 2", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(3, 152, "Колода номер 3", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(4, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(5, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(6, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(7, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(8, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(9, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(10, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(11, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(12, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(13, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(14, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(15, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(16, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(17, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(18, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(19, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(20, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(21, 152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundScreen)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .statusBarsPadding()
    ) {
        ActionBar(
            onBackClick = { navController.popBackStack() },
            onMenuClick = { },
            containerModifier = Modifier
                .fillMaxWidth(),
            iconModifier = Modifier
                .clip(shape = RoundedCornerShape(50.dp))
                .background(color = Blue, shape = RoundedCornerShape(50.dp))
                .padding(all = 12.dp)
                .size(size = 16.dp),
        )
        Text(
            text = stringResource(R.string.title_text_folders),
            style = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        )
        Spacer(Modifier.height(18.dp))
        DisplayItemCount(
            pluralsTextOne = R.plurals.folder_amount,
            pluralsTextTwo = R.plurals.deck_amount,
            listOne = folders,
            listTwo = listOf(1),
            textStyle = textStyle
        )
        LazyColumn {
            items(items = folders, key = { it.id }) {
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
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.opensans_medium))
                    ),
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}
