package com.mindeck.presentation.ui.components.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.ui.theme.MindeckTheme

@Composable
fun SelectorRow(
    label: String,
    selectedText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(MindeckTheme.dimensions.paddingXs),
        )

        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.extraSmall,
                )
                .size(
                    height = MindeckTheme.dimensions.dp36,
                    width = MindeckTheme.dimensions.dp200,
                )
                .border(
                    width = MindeckTheme.dimensions.dp0_25,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.extraSmall,
                )
                .wrapContentSize(Alignment.Center),
        ) {
            Text(
                text = selectedText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectorRowPreview() {
    MindeckTheme {
        SelectorRow(
            modifier = Modifier.padding(MindeckTheme.dimensions.paddingMd),
            label = "Колода",
            selectedText = "Английский",
            onClick = {},
        )
    }
}
