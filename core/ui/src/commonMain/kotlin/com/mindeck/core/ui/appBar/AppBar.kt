package com.mindeck.core.ui.appBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
    leadingAction: AppBarAction,
    trailingAction: AppBarAction,
    modifier: Modifier = Modifier,
    navigationIcon: DrawableResource? = null,
    onNavigateBack: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (onNavigateBack != null) {
            IconButton(onClick = { onNavigateBack() }) {
                navigationIcon?.let { Icon(painter = painterResource(it), contentDescription = "") }
            }
        } else {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXs)) {
            IconButton(onClick = leadingAction.onClick) {
                Icon(painter = painterResource(leadingAction.icon), contentDescription = "")
            }
            IconButton(onClick = trailingAction.onClick) {
                Icon(painter = painterResource(trailingAction.icon), contentDescription = "")
            }
        }
    }
}

data class AppBarAction(
    val icon: DrawableResource,
    val onClick: () -> Unit
)