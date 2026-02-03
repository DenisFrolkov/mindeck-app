package com.mindeck.presentation.ui.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.utils.dimenDpResource

@Composable
fun SaveDataButton(
    text: String,
    textStyle: TextStyle,
    buttonModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
) {
    Box(
        modifier = buttonModifier,
    ) {
        Text(
            text = text,
            style = textStyle,
            modifier = textModifier.padding(
                vertical = dimenDpResource(R.dimen.padding_small),
                horizontal = dimenDpResource(R.dimen.padding_extra_large),
            ),
        )
    }
}
