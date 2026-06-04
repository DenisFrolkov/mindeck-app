package com.mindeck.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import com.mindeck.core.ui.appBar.AppBar
import com.mindeck.core.ui.appBar.AppBarAction
import com.mindeck.core.ui.button.AppButton
import com.mindeck.core.ui.button.AppFAB
import com.mindeck.core.ui.label.TagLabel
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.check_circle_icon
import mindeck_app.core.ui.generated.resources.library_decks_icon
import mindeck_app.core.ui.generated.resources.play_arrow_icon
import mindeck_app.core.ui.generated.resources.search_icon
import mindeck_app.core.ui.generated.resources.settings_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainScreen(
    onNavigateToSecond: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        modifier =
            modifier
                .fillMaxSize()
                .padding(MindeckTheme.dimensions.screenPadding),
        topBar = {
            AppBar(
                title = "Mindeck",
                leadingAction = AppBarAction(
                    icon = Res.drawable.search_icon,
                    onClick = { }
                ),
                trailingAction = AppBarAction(
                    icon = Res.drawable.settings_icon,
                    onClick = { }
                ),
                modifier = Modifier.statusBarsPadding(),
            )
        },
        floatingActionButton = {
            AppFAB(
                onClick = onNavigateToSecond
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXxxl)
        ) {
            DailyReviewCard(
                modifier = Modifier.padding(top = MindeckTheme.dimensions.spacingMd),
                onNavigateToSecond = onNavigateToSecond,
            )
            Decks(onAllDecksClick = { })
        }
    }
}

@Composable
fun DailyReviewCard(
    onNavigateToSecond: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MindeckTheme.shapes.hero,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(
                        start = MindeckTheme.dimensions.spacingXl,
                        top = MindeckTheme.dimensions.spacingLg,
                        end = MindeckTheme.dimensions.spacingXl,
                        bottom = MindeckTheme.dimensions.spacingLg,
                    ),
        ) {
            Text(
                text = "СЕГОДНЯ К ПОВТОРЕНИЮ",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXs)
            ) {
                Text(
                    text = "40",
                    modifier = Modifier.alignByBaseline(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style =
                        MaterialTheme.typography.displayLarge.copy(
                            lineHeightStyle =
                                LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Bottom,
                                    trim = LineHeightStyle.Trim.Both,
                                ),
                        ),
                )
                Text(
                    text = "карточек",
                    modifier = Modifier.alignByBaseline(),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingLg),
                verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXs),
            ) {
                TagLabel(MaterialTheme.colorScheme.primary, "7 новых")
                TagLabel(
                    MaterialTheme.colorScheme.primary.copy(alpha = .75f),
                    "8 новых на повторение"
                )
                TagLabel(MaterialTheme.colorScheme.primary.copy(alpha = .5f), "25 на повторение")
            }

            VerticalSpacer(MindeckTheme.dimensions.spacingXl)

            AppButton(
                onAction = onNavigateToSecond,
                buttonText = "Начать повторение",
                buttonIcon = Res.drawable.play_arrow_icon,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun Decks(onAllDecksClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingLg),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "КОЛОДЫ",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = MindeckTheme.dimensions.spacingSm)
            )
            Row(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable { onAllDecksClick() }
                    .padding(end = MindeckTheme.dimensions.spacingLg),
                horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingSm)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.library_decks_icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(MindeckTheme.dimensions.iconXs),
                )
                Text(
                    text = "Все",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingSm)
        ) {
            item {
                DeckItem(
                    title = "Испанский базовая логика",
                    count = 124,
                    review = 18,
                )
            }
        }
    }
}

@Composable
fun DeckItem(title: String, count: Int, review: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MindeckTheme.shapes.card,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MindeckTheme.dimensions.spacingMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingMd)
            ) {
                Box(
                    modifier = Modifier
                        .size(MindeckTheme.dimensions.touchTarget)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.medium
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title.firstOrNull()?.toString() ?: "",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXxs)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .75f)
                    )
                    Text(
                        text = "$count карточки",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            if (review != 0) {
                Box(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ).padding(
                        vertical = MindeckTheme.dimensions.spacingXs,
                        horizontal = MindeckTheme.dimensions.spacingLg
                    )
                ) {
                    Text(
                        text = review.toString(),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            } else {
                Icon(
                    painter = painterResource(Res.drawable.check_circle_icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
