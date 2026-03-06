package com.mindeck.presentation.ui.components.repeatOptions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.mindeck.presentation.R

@Composable
fun RepeatOptionsButton(
    buttonColor: Color,
    label: String,
    textStyle: TextStyle,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.extraSmall)
            .background(color = buttonColor, shape = MaterialTheme.shapes.extraSmall)
            .border(
                width = dimensionResource(R.dimen.dimen_0_25),
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.extraSmall,
            )
            .width(dimensionResource(R.dimen.dimen_78))
            .clickable {
                onClick()
            }
            .padding(vertical = dimensionResource(R.dimen.dimen_6))
            .wrapContentSize(Alignment.Center),
    ) {
        Text(
            text = label,
            style = textStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = dimensionResource(R.dimen.dimen_8)),
        )
    }
}
