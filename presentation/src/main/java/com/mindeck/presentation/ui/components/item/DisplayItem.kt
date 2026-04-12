package com.mindeck.presentation.ui.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.MindeckTheme

@Composable
fun DisplayItem(
    icon: Int,
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .border(
                width = MindeckTheme.dimensions.dp0_25,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small,
            )
            .clip(shape = MaterialTheme.shapes.small)
            .height(MindeckTheme.dimensions.dp48)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = MindeckTheme.dimensions.dp12),
        ) {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            thickness = MindeckTheme.dimensions.dp0_25,
            color = MaterialTheme.colorScheme.outline,
        )

        Box(
            modifier = Modifier
                .size(MindeckTheme.dimensions.dp48)
                .background(color = MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(MindeckTheme.dimensions.iconMd),
                painter = painterResource(icon),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DisplayItemPreview() {
    MindeckTheme {
        DisplayItem(
            modifier = Modifier.padding(MindeckTheme.dimensions.paddingMd),
            icon = R.drawable.card_icon,
            name = "Английский язык — базовый курс",
            onClick = {},
        )
    }
}
