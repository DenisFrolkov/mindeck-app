package com.mindeck.core.ui.button

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.add_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
    ) {
        Icon(
            painter = painterResource(Res.drawable.add_icon),
            contentDescription = "Добавить",
        )
    }
}
