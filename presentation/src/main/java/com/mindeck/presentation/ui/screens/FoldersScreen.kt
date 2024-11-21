package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.theme.BackgroundScreen
import com.mindeck.presentation.ui.theme.Black
import com.mindeck.presentation.ui.theme.Blue

@Composable
fun FoldersScreen() {
    var fontFamily = FontFamily(Font(R.font.opensans_medium))
    var textStyle = TextStyle(fontSize = 14.sp, color = Black, fontFamily = fontFamily)

    var foldersWord: List<String> = listOf("Папка", "Папки", "Папок")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundScreen)
            .statusBarsPadding()
            .padding(start = 16.dp, top = 20.dp, end = 16.dp)
    ) {
        item {
            ActionHandlerButton(
                iconPainter = painterResource(R.drawable.back_icon),
                contentDescription = stringResource(R.string.back_screen_icon_button),
                boxModifier = Modifier
                    .padding(start = 14.dp, top = 14.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { },
                iconModifier = Modifier
                    .background(color = Blue, shape = RoundedCornerShape(50.dp))
                    .padding(all = 12.dp)
                    .size(size = 16.dp)
            )
            Text(
                text = stringResource(R.string.title_text_folders),
                style = textStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "3 ${getPluralForm(3, foldersWord)}",
                style = textStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

fun getPluralForm(number: Int, forms: List<String>): String {
    val n = number % 100
    val n1 = number % 10
    return when {
        n in 11..19 -> forms[2]
        n1 == 1 -> forms[0]
        n1 in 2..4 -> forms[1]
        else -> forms[2]
    }
}