package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
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
import com.mindeck.presentation.ui.components.repeat_options.RepeatOptionData
import com.mindeck.presentation.ui.components.repeat_options.RepeatOptionsButton
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.theme.background_light_blue
import com.mindeck.presentation.ui.theme.outline_variant_blue
import com.mindeck.presentation.ui.theme.repeat_button_light_blue
import com.mindeck.presentation.ui.theme.repeat_button_light_mint
import com.mindeck.presentation.ui.theme.repeat_button_light_red
import com.mindeck.presentation.ui.theme.repeat_button_light_yellow
import com.mindeck.presentation.ui.theme.outline_medium_gray
import com.mindeck.presentation.ui.theme.on_primary_white

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
        RepeatOptionData(title = "Повторить", time = "1 минута", color = repeat_button_light_blue, action = { }),
        RepeatOptionData(title = "Легко", time = "5 дней", color = repeat_button_light_mint, action = { }),
        RepeatOptionData(title = "Средне", time = "2 дней", color = repeat_button_light_yellow, action = { }),
        RepeatOptionData(title = "Тяжело", time = "1 дней", color = repeat_button_light_red, action = { })
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background_light_blue)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        containerColor = background_light_blue,
        topBar = {
            ActionBar(
                onBackClick = { navController.popBackStack() },
                onMenuClick = { },
                containerModifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                iconModifier = Modifier
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(color = outline_variant_blue, shape = RoundedCornerShape(50.dp))
                    .padding(all = 12.dp)
                    .size(size = 16.dp),
            )
            Spacer(Modifier.height(14.dp))
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding).verticalScroll(state = scrollState)) {
                Column(modifier = Modifier.padding(padding)) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        QuestionAndAnswerElement(
                            question = "43214123",
                            answer = "43214123",
                            questionStyle = questionStyle,
                            answerStyle = answerStyle,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(on_primary_white)
                                .border(
                                    width = 0.5.dp,
                                    color = outline_medium_gray,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)
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
    )
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
            color = outline_medium_gray
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

