package com.mindeck.presentation.ui.components.dropdown_selector

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.ui.theme.MediumGray
import com.mindeck.presentation.ui.theme.White

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun DropdownMenu(
    selectorItemList: List<String>,
    dropdownState: DropdownState,
    onStringClick: (String) -> Unit,
    modifier: Modifier,
    textStyle: TextStyle,
) {
    val animetedHeightIn = animateDropdownWidth(dropdownState.dropdownHeight, dropdownState.animationDuration)
    val offsetY = animateDropdownOffsetY(dropdownState.dropdownOffsetY, dropdownState.animationDuration)
    val alpha = animateDropdownAlpha(dropdownState.dropdownAlpha, dropdownState.animationDuration)

    val baseItemModifier = modifier
        .fillMaxWidth()
        .height(36.dp)
        .alpha(alpha)
        .offset(y = -offsetY)

    LaunchedEffect(selectorItemList) {
        dropdownState.open()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = animetedHeightIn)
    ) {
        items(selectorItemList) {
            val isLastItem = it == selectorItemList.last()

            Box(
                contentAlignment = Alignment.Center,
                modifier = baseItemModifier
                    .background(
                        color = White,
                        shape = (if (isLastItem) RoundedCornerShape(
                            bottomStart = 4.dp,
                            bottomEnd = 4.dp
                        ) else RoundedCornerShape(size = 0.dp))
                    )
                    .drawBehind {
                        val borderThickness = 0.25.dp.toPx()
                        drawLine(
                            color = MediumGray,
                            start = Offset(0f, size.height - borderThickness / 2),
                            end = Offset(size.width, size.height - borderThickness / 2),
                            strokeWidth = borderThickness
                        )
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        dropdownState.reset()
                        onStringClick(it)
                    }
            ) {
                Text(
                    it, style = textStyle
                )
            }
        }
    }
}