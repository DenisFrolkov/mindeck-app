package com.mindeck.presentation.ui.components.dropdown_selector

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.MediumGray
import com.mindeck.presentation.ui.theme.White

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun DropdownMenu(
    modifier: Modifier,
    selectorItemList: List<String>,
    onStringClick: (String) -> Unit,
    fontFamily: FontFamily,
    dropdownState: DropdownState
) {
    val animetedHeightIn = animateDropdownWidth(dropdownState.dropdownHeight, dropdownState.animationDuration)
    val offsetY = animateDropdownOffsetY(dropdownState.dropdownOffsetY, dropdownState.animationDuration)
    val alpha = animateDropdownAlpha(dropdownState.dropdownAlpha, dropdownState.animationDuration)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = animetedHeightIn)
    ) {
        items(selectorItemList) {
            LaunchedEffect(it) {
                dropdownState.open()
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .offset(y = -offsetY)
                    .alpha(alpha)
                    .background(
                        color = White,
                        shape = (if (it == selectorItemList.last()) RoundedCornerShape(
                            bottomStart = 4.dp,
                            bottomEnd = 4.dp
                        ) else RoundedCornerShape(0.dp))
                    )
                    .height(height = 36.dp)
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
                    it, style = TextStyle(
                        fontSize = 14.sp, fontFamily = fontFamily
                    )
                )
            }
        }
    }
}