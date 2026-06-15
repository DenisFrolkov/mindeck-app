package com.mindeck.core.ui.appBar

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mindeck.core.ui.theme.MindeckTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigation: AppBarAction? = null,
    actions: List<AppBarAction> = emptyList(),
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (navigation != null) {
                ActionButton(navigation)
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXs)) {
            actions.forEach { ActionButton(it) }
        }
    }
}

@Composable
private fun ActionButton(action: AppBarAction) {
    IconButton(onClick = action.onClick) {
        Icon(
            painter = painterResource(action.icon),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = action.contentDescription,
            modifier = Modifier.size(MindeckTheme.dimensions.iconMd)
        )
    }
}

data class AppBarAction(
    val icon: DrawableResource,
    val contentDescription: String,
    val onClick: () -> Unit,
)
