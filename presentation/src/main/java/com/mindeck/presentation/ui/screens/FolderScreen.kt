package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.folder.DisplayCardFolder
import com.mindeck.presentation.ui.components.folder.FolderData
import com.mindeck.presentation.ui.components.utils.getPluralForm
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.BackgroundScreen
import com.mindeck.presentation.ui.theme.Black
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.LightBlue

@Composable
fun FolderScreen(navController: NavController) {
    var fontFamily = FontFamily(Font(R.font.opensans_medium))
    var textStyle = TextStyle(fontSize = 14.sp, color = Black, fontFamily = fontFamily)

    val decks = listOf(
        FolderData(123, "Общая колода", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(152, "Колода номер 1", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(152, "Колода номер 2", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(152, "Колода номер 3", LightBlue, Blue, R.drawable.deck_icon),
        FolderData(152, "Колода номер 4", LightBlue, Blue, R.drawable.deck_icon)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundScreen)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(Modifier.height(34.dp))
            ActionBar(
                onBackClick = { navController.popBackStack() },
                onMenuClick = {  },
                containerModifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                iconModifier = Modifier
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(color = Blue, shape = RoundedCornerShape(50.dp))
                    .padding(all = 12.dp)
                    .size(size = 16.dp),
            )
            Text(
                text = "Название колоды",
                style = textStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
            Spacer(Modifier.height(18.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = pluralStringResource(R.plurals.deck_amount, getPluralForm(decks.size), decks.size),
                    style = textStyle,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = Black,
                            shape = RoundedCornerShape(50.dp)
                        )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = pluralStringResource(R.plurals.card_amount, getPluralForm(1), 1),
                    style = textStyle,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
        items(decks) {
            DisplayCardFolder(
                folderIcon =
                painterResource(it.icon),
                numberOfCards = it.countCard,
                folderName = it.text,
                backgroundColor = it.color,
                iconColor = it.colorTwo,
                onClick = {
                    navController.navigate(NavigationRoute.DeckScreen.route)
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