package com.mindeck.presentation.ui.components.dropdown

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mindeck.presentation.ui.theme.MindeckTheme

@Composable
fun AppDropdownMenu(
    padding: PaddingValues,
    isExpanded: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(horizontal = MindeckTheme.dimensions.dp28)
            .padding(padding)
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd),
    ) {
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismiss,
            containerColor = MaterialTheme.colorScheme.onPrimary,
            shape = MaterialTheme.shapes.medium,
        ) {
            content()
        }
    }
}
