package com.mindeck.presentation.ui.components.dropdown_selector

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
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
fun DropDownMenu(
    modifier: Modifier,
    selectorItemList: List<String>,
    onClick: (Boolean) -> Unit
) {

    val maxVisibleItems = 7
    val itemHeight = 36.dp
    val lazyColumnMaxHeight = maxVisibleItems * itemHeight  - 14.dp
    val listState = rememberLazyListState()

    Box() {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = lazyColumnMaxHeight)
        ) {
            items(selectorItemList) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .background(
                            color = White, shape = (if (it == "23123124") RoundedCornerShape(
                                bottomStart =  4.dp,
                                bottomEnd =  4.dp
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
                            onClick(true)
                        }
                ) {
                    Text(
                        it, style = TextStyle(
                            fontSize = 14.sp, fontFamily = FontFamily(
                                Font(R.font.opensans_medium)
                            )
                        )
                    )
                }
            }
        }
    }
}