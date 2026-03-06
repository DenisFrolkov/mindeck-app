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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.mindeck.presentation.R

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
                .padding(dimensionResource(R.dimen.padding_small))
                .wrapContentSize(Alignment.CenterStart),
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = dimensionResource(R.dimen.horizontal_divider_dot_two_five_height),
            color = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = answer,
            style = answerStyle,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
                .wrapContentSize(Alignment.CenterStart),
        )
    }
}
