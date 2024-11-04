package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.BackgroundScreen
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.MediumGray
import com.mindeck.presentation.ui.theme.White

@Composable
fun CreationCardScreen() {
    val insets = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundScreen)
            .padding(top = insets)
    ) {
        BackScreenButton()

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownSelector(titleSelector = "Папка:", "Общая папка", modifier = Modifier)
            }
        }
    }
}

@Composable
private fun DropdownSelector(titleSelector: String, selectorItem: String, modifier: Modifier) {

    var clickDropdown by remember { mutableStateOf(true) }

    val selectorItemList = listOf(
        "123",
        "fasfdf",
        "23123124",
        "123",
        "fasfdf",
        "23123124",
        "123",
        "fasfdf",
        "23123124",
        "123",
        "fasfdf",
        "23123124"
    )

    Row(
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.padding(start = 0.dp, top = 5.dp)) {
            Text(
                titleSelector, style = TextStyle(
                    fontSize = 12.sp, fontFamily = FontFamily(
                        Font(R.font.opensans_medium)
                    )
                )
            )
        }
        Spacer(modifier = Modifier.width(5.dp))

        Column {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .background(color = White, shape = RoundedCornerShape(4.dp))
                    .size(width = 120.dp, height = 30.dp)
                    .border(
                        width = 0.25.dp,
                        color = Color.Black,
                        shape = (if (clickDropdown) RoundedCornerShape(4.dp) else RoundedCornerShape(
                            4.dp,
                            4.dp,
                            0.dp,
                            0.dp
                        ))
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        clickDropdown = false
                    }
            ) {
                Text(
                    text = selectorItem, style = TextStyle(
                        fontSize = 12.sp, fontFamily = FontFamily(
                            Font(R.font.opensans_medium)
                        )
                    )
                )
            }
            if (clickDropdown == false) {
                Column(
                    modifier = Modifier.border(
                        width = 0.25.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(
                            0.dp,
                            0.dp,
                            4.dp,
                            4.dp
                        )
                    )
                ) {
                    DropDownMenu(modifier, selectorItemList) {
                        clickDropdown = it
                    }
                }
            }
        }
    }
}

@Composable
private fun DropDownMenu(
    modifier: Modifier,
    selectorItemList: List<String>,
    onClick: (Boolean) -> Unit
) {

    val maxVisibleItems = 6
    val itemHeight = 30.dp
    val lazyColumnMaxHeight = maxVisibleItems * itemHeight
    val listState = rememberLazyListState()

    Box() {
        LazyColumn(
            state = listState,
            modifier = Modifier.heightIn(max = lazyColumnMaxHeight)
        ) {
            items(selectorItemList) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .background(
                            color = White, shape = (if (it == "23123124") RoundedCornerShape(
                                0.dp,
                                0.dp,
                                4.dp,
                                4.dp
                            ) else RoundedCornerShape(0.dp))
                        )
                        .size(width = 120.dp, height = 30.dp)
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
                            fontSize = 12.sp, fontFamily = FontFamily(
                                Font(R.font.opensans_medium)
                            )
                        )
                    )
                }
            }
        }

        val totalItemsCount = selectorItemList.size
        val indicatorHeightFraction = maxVisibleItems.toFloat() / totalItemsCount.toFloat()
        val indicatorHeight = lazyColumnMaxHeight * indicatorHeightFraction

        // Вычисляем фактическое смещение, чтобы индикатор корректно следовал за скроллом
        val indicatorOffset = listState.firstVisibleItemIndex.toFloat() / (totalItemsCount - maxVisibleItems).toFloat()

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(
                    y = with(LocalDensity.current) {
                        ((indicatorOffset * (lazyColumnMaxHeight - indicatorHeight - 7.5.dp)) * 2)
                    }
                )
                .height(15.dp)
                .width(3.dp)
                .background(Color.Gray.copy(alpha = 0.5f))
        )
    }
}


@Composable
private fun CardTypeField(filedTitle: String, field: String, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            filedTitle, style = TextStyle(
                fontSize = 12.sp, fontFamily = FontFamily(
                    Font(R.font.opensans_medium)
                )
            )
        )
        Spacer(modifier = Modifier.width(5.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .background(color = White, shape = RoundedCornerShape(4.dp))
                .size(width = 120.dp, height = 30.dp)
                .border(
                    width = 0.25.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Text(
                field, style = TextStyle(
                    fontSize = 12.sp, fontFamily = FontFamily(
                        Font(R.font.opensans_medium)
                    )
                )
            )
        }
    }
}

@Composable
private fun BackScreenButton() {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .padding(14.dp)
            .clip(shape = RoundedCornerShape(50.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {

            }
    ) {
        Icon(
            painter = painterResource(R.drawable.back_icon),
            tint = White,
            contentDescription = "Back To Previous Page",
            modifier = Modifier
                .background(color = Blue, shape = RoundedCornerShape(50.dp))
                .padding(all = 12.dp)
                .size(size = 16.dp)
        )
    }
}