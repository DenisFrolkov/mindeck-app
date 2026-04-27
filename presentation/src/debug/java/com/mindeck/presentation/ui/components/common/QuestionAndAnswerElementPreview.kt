package com.mindeck.presentation.ui.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.ui.theme.MindeckTheme

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
