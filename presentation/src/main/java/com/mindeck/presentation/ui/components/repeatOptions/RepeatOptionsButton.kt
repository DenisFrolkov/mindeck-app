package com.mindeck.presentation.ui.components.repeatOptions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.MindeckTheme

@Composable
fun RepeatOptionsButton(
    buttonColor: Color,
    label: String,
    time: String,
    textStyle: TextStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(color = buttonColor)
            .border(
                width = dimensionResource(R.dimen.dimen_0_25),
                color = MaterialTheme.colorScheme.outline,
            )
            .clickable {
                onClick()
            }
            .padding(vertical = dimensionResource(R.dimen.dimen_4))
            .wrapContentSize(Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_4)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = textStyle,
            modifier = Modifier,
        )
        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RepeatOptionsButtonPreview() {
    MindeckTheme {
        RepeatOptionsButton(
            buttonColor = Color(0xFFB5EAD7),
            label = "Хорошо",
            time = "1 д",
            textStyle = MaterialTheme.typography.bodyMedium,
            onClick = {},
        )
    }
}
