package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
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
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.theme.outline_variant_blue
import com.mindeck.presentation.ui.theme.repeat_button_light_blue
import com.mindeck.presentation.ui.theme.repeat_button_light_mint
import com.mindeck.presentation.ui.theme.repeat_button_light_red
import com.mindeck.presentation.ui.theme.repeat_button_light_yellow
import com.mindeck.presentation.ui.theme.outline_medium_gray

@Composable
fun CardStudyScreen(navController: NavController) {

    val scrollState = rememberScrollState()

    var repeatOptionsButton = listOf(
        RepeatOptionData(
            title = stringResource(R.string.repeat_option_title_repeat_text),
            time = stringResource(R.string.repeat_option_time_one_minute_text),
            color = repeat_button_light_blue,
            action = { }),
        RepeatOptionData(
            title = stringResource(R.string.repeat_option_title_easy_text),
            time = stringResource(R.string.repeat_option_time_five_day_text),
            color = repeat_button_light_mint,
            action = { }),
        RepeatOptionData(
            title = stringResource(R.string.repeat_option_title_medium_text),
            time = stringResource(R.string.repeat_option_time_two_day_text),
            color = repeat_button_light_yellow,
            action = { }),
        RepeatOptionData(
            title = stringResource(R.string.repeat_option_title_hard_text),
            time = stringResource(R.string.repeat_option_time_one_day_text),
            color = repeat_button_light_red,
            action = { })
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ActionBar(
                onBackClick = { navController.popBackStack() },
                onMenuClick = { },
                containerModifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimenDpResource(R.dimen.padding_medium)),
                iconModifier = Modifier
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .background(color = outline_variant_blue, shape = MaterialTheme.shapes.extraLarge)
                    .padding(all = dimenDpResource(R.dimen.padding_small))
                    .size(size = dimenDpResource(R.dimen.padding_medium)),
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(state = scrollState)
            ) {
                Column(modifier = Modifier.padding(padding)) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        QuestionAndAnswerElement(
                            question = "43214123",
                            answer = "43214123",
                            questionStyle = MaterialTheme.typography.bodyMedium.copy(
                                textAlign = TextAlign.Center
                            ),
                            answerStyle = MaterialTheme.typography.bodyMedium.copy(
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.onPrimary)
                                .border(
                                    dimenDpResource(R.dimen.border_width_dot_five),
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.shapes.extraSmall
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
                    .padding(bottom = dimenDpResource(R.dimen.padding_medium))
            ) {
                repeatOptionsButton.forEach {
                    RepeatOptionsButton(
                        buttonColor = it.color,
                        textDifficultyOfRepetition = it.title,
                        repeatTimeText = it.time,
                        onClick = it.action,
                        titleTextStyle = MaterialTheme.typography.labelMedium.copy(
                            textAlign = TextAlign.Center
                        ),
                        subtitleTextStyle = MaterialTheme.typography.labelSmall.copy(
                            textAlign = TextAlign.Center
                        )
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
                .padding(dimenDpResource(R.dimen.padding_extra_small))
                .wrapContentSize(Alignment.Center)
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = dimenDpResource(R.dimen.horizontal_divider_dot_two_five_height),
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = answer,
            style = answerStyle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimenDpResource(R.dimen.padding_extra_small))
                .wrapContentSize(Alignment.Center)
        )
    }
}

