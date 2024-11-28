package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.common.DisplayItemCount
import com.mindeck.presentation.ui.components.folder.DeckData
import com.mindeck.presentation.ui.components.folder.DisplayCardItem
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.BackgroundScreen
import com.mindeck.presentation.ui.theme.Black
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.LightBlue

@Composable
fun DeckScreen(navController: NavController) {
    var fontFamily = FontFamily(Font(R.font.opensans_medium))
    var textStyle = TextStyle(fontSize = 14.sp, color = Black, fontFamily = fontFamily)

    val cards = listOf(
        DeckData(1, "Картчока номер 0", R.drawable.card_icon),
        DeckData(2, "Картчока номер 1", R.drawable.card_icon),
        DeckData(3, "Картчока номер 1", R.drawable.card_icon),
        DeckData(4, "Картчока номер 1", R.drawable.card_icon),
        DeckData(5, "Картчока номер 1", R.drawable.card_icon),
        DeckData(6, "Картчока номер 1", R.drawable.card_icon),
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
            text = "Название колоды",
            style = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        )
        Spacer(Modifier.height(18.dp))
        DisplayItemCount(
            pluralsTextOne = R.plurals.card_amount,
            listOne = cards,
            textStyle = textStyle
        )
        LazyColumn {
            items(items = cards, key = { it.id }) {
                DisplayCardItem(
                    cardIcon =
                    painterResource(it.icon),
                    titleCard = it.title,
                    backgroundColor = LightBlue,
                    iconColor = Blue,
                    onClick = {
                        navController.navigate(NavigationRoute.CreationCardScreen.route)
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