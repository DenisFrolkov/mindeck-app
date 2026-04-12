package com.mindeck.presentation.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.ui.theme.MindeckTheme

@Composable
fun QuestionAndAnswerElement(
    question: String,
    answer: String,
    questionStyle: TextStyle,
    answerStyle: TextStyle,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = question,
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
        Text(
            text = answer,
            style = answerStyle,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MindeckTheme.dimensions.paddingSm)
                .wrapContentSize(Alignment.CenterStart),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuestionAndAnswerElementPreview() {
    MindeckTheme {
        QuestionAndAnswerElement(
            question = "Что такое Coroutine?",
            answer = "Лёгковесная сопрограмма для асинхронного кода",
            questionStyle = MaterialTheme.typography.bodyMedium,
            answerStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().padding(MindeckTheme.dimensions.paddingMd),
        )
    }
}
