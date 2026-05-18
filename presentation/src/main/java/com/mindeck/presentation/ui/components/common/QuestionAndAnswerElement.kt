package com.mindeck.presentation.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

@OptIn(ExperimentalRichTextApi::class)
@Composable
fun QuestionAndAnswerElement(
    questionState: RichTextState,
    answerState: RichTextState,
    question: String,
    answer: String,
    questionStyle: TextStyle,
    answerStyle: TextStyle,
    modifier: Modifier,
) {
    LaunchedEffect(question) {
        questionState.setHtml(question)
    }

    LaunchedEffect(answer) {
        answerState.setHtml(answer)
    }

    Column(
        modifier = modifier,
    ) {
        RichText(
            state = questionState,
            style = questionStyle,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MindeckTheme.dimensions.paddingSm)
                .wrapContentSize(Alignment.CenterStart),
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = MindeckTheme.dimensions.dp0_25,
            color = MaterialTheme.colorScheme.outline,
        )
        RichText(
            state = answerState,
            style = answerStyle,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MindeckTheme.dimensions.paddingSm)
                .wrapContentSize(Alignment.CenterStart),
        )
    }
}
