package com.mindeck.presentation.ui.components.dropdown.dropdown_selector

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.ui.theme.MediumGray
import com.mindeck.presentation.ui.theme.White

@Composable
fun DropdownSelector(
    dropdownSelectorDataClass: DropdownSelectorDataClass,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier
) {
    val dropdownState = remember { DropdownState() }

    Row() {
        Text(
            dropdownSelectorDataClass.title, style = textStyle,
            modifier = titleModifier
                .padding(8.5.dp)
                .wrapContentSize(Alignment.CenterStart)
                .width(120.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                dropdownState.toggle()
            }) {
            Box(
                modifier = baseSelectorModifier(dropdownState.isExpanded)
            ) {
                Text(
                    text = dropdownSelectorDataClass.selectedItem, style = textStyle
                )
            }
            if (dropdownState.isExpanded) {
                Column(
                    modifier = baseSelectorItemModifier()
                ) {
                    DropdownMenu(
                        selectorItemList = dropdownSelectorDataClass.itemList,
                        onStringClick = dropdownSelectorDataClass.onItemClick,
                        dropdownState = dropdownState,
                        modifier = modifier,
                        textStyle = textStyle
                    )
                }
            }
        }
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
private fun DropdownMenu(
    selectorItemList: List<String>,
    dropdownState: DropdownState,
    onStringClick: (String) -> Unit,
    modifier: Modifier,
    textStyle: TextStyle,
) {
    val animatedHeightIn = animateDropdownHeightIn(dropdownState.dropdownHeight, dropdownState.animationDuration)
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
            .heightIn(max = animatedHeightIn)
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
                        onStringClick(it)
                        dropdownState.reset()
                    }
            ) {
                Text(
                    it, style = textStyle
                )
            }
        }
    }
}

@Composable
fun baseSelectorModifier(isExpanded: Boolean) = Modifier
        .fillMaxWidth()
        .background(color = White, shape = RoundedCornerShape(4.dp))
        .height(height = 36.dp)
        .border(
            width = 0.25.dp,
            color = Color.Black,
            shape = (if (isExpanded) RoundedCornerShape(
                topStart = 4.dp,
                topEnd = 4.dp,
            ) else RoundedCornerShape(4.dp))
        )
        .wrapContentSize(Alignment.Center)


@Composable
fun baseSelectorItemModifier() = Modifier
        .border(
            width = 0.25.dp,
            color = Color.Black,
            shape = RoundedCornerShape(
                bottomStart = 4.dp,
                bottomEnd = 4.dp
            )
        )