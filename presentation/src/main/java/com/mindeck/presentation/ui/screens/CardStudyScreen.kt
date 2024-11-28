package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.RepeatOptions.RepeatOptionData
import com.mindeck.presentation.ui.components.RepeatOptions.RepeatOptionsButton
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.theme.BackgroundScreen
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.LightBlue
import com.mindeck.presentation.ui.theme.LightMint
import com.mindeck.presentation.ui.theme.LightRed
import com.mindeck.presentation.ui.theme.LightYellow
import com.mindeck.presentation.ui.theme.MediumGray
import com.mindeck.presentation.ui.theme.White

@Composable
fun CardStudyScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val fontFamily = remember { FontFamily(Font(R.font.opensans_medium)) }
    val questionStyle =
        remember {
            TextStyle(
                fontSize = 14.sp,
                fontFamily = fontFamily,
                textAlign = TextAlign.Center
            )
        }
    val answerStyle =
        remember {
            TextStyle(
                fontSize = 14.sp,
                fontFamily = fontFamily,
                textAlign = TextAlign.Center
            )
        }

    var repeatOptionsButton = listOf(
        RepeatOptionData(title = "Повторить", time = "1 минута", color = LightBlue, action = { }),
        RepeatOptionData(title = "Легко", time = "5 дней", color = LightMint, action = { }),
        RepeatOptionData(title = "Средне", time = "2 дней", color = LightYellow, action = { }),
        RepeatOptionData(title = "Тяжело", time = "1 дней", color = LightRed, action = { })
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundScreen)
            .verticalScroll(state = scrollState)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        ActionBar(
            onBackClick = { navController.popBackStack() },
            onMenuClick = { },
            containerModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            iconModifier = Modifier
                .clip(shape = RoundedCornerShape(50.dp))
                .background(color = Blue, shape = RoundedCornerShape(50.dp))
                .padding(all = 12.dp)
                .size(size = 16.dp),
        )
        Spacer(Modifier.height(14.dp))
        QuestionAndAnswerElement(
            question = "1243213",
            answer = "43214123",
            questionStyle = questionStyle,
            answerStyle = answerStyle,
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .border(width = 0.5.dp, color = MediumGray, shape = RoundedCornerShape(4.dp))
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
                .height(60.dp)
        ) {
            repeatOptionsButton.forEach {
                RepeatOptionsButton(
                    buttonColor = it.color,
                    textDifficultyOfRepetition = it.title,
                    repeatTimeText = it.time,
                    fontFamily = fontFamily,
                    onClick = it.action
                )
            }
        }
    }
}

@Composable
private fun QuestionAndAnswerElement(
    question: String,
    answer: String,
    questionStyle: TextStyle,
    answerStyle: TextStyle,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = question,
            style = questionStyle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .wrapContentSize(Alignment.Center)
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.5.dp,
            color = MediumGray
        )
        Text(
            text = answer,
            style = answerStyle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .wrapContentSize(Alignment.Center)
        )
    }
}

