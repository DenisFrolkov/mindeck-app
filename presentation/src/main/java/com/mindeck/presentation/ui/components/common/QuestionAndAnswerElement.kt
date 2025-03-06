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
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.utils.dimenDpResource

@Composable
fun QuestionAndAnswerElement(
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
                .padding(dimenDpResource(R.dimen.padding_small))
                .wrapContentSize(Alignment.CenterStart)
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
                .padding(dimenDpResource(R.dimen.padding_small))
                .wrapContentSize(Alignment.CenterStart)
        )
    }
}
