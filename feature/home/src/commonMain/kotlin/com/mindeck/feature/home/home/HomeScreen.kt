package com.mindeck.feature.home.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.sp
import com.mindeck.core.ui.appBar.AppBar
import com.mindeck.core.ui.appBar.AppBarAction
import com.mindeck.core.ui.button.AppButton
import com.mindeck.core.ui.button.AppFAB
import com.mindeck.core.ui.label.TagLabel
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.bar_chart_icon
import mindeck_app.core.ui.generated.resources.check_circle_icon
import mindeck_app.core.ui.generated.resources.cloud_off_icon
import mindeck_app.core.ui.generated.resources.library_decks_icon
import mindeck_app.core.ui.generated.resources.play_arrow_icon
import mindeck_app.core.ui.generated.resources.refresh_icon
import mindeck_app.core.ui.generated.resources.search_icon
import mindeck_app.core.ui.generated.resources.settings_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(
    state: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
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
                leadingAction =
                    AppBarAction(
                        icon = Res.drawable.search_icon,
                        onClick = { },
                    ),
                trailingAction =
                    AppBarAction(
                        icon = Res.drawable.settings_icon,
                        onClick = { },
                    ),
                modifier = Modifier.statusBarsPadding(),
            )
        },
        floatingActionButton = {
            AppFAB(onClick = onNavigateToSecond)
        },
    ) { paddingValues ->
        when (state) {
            HomeUiState.Loading -> HomeLoading(modifier = modifier,)
            is HomeUiState.Error ->
                HomeError(
                    message = state.message,
                    onRetry = { onIntent(HomeIntent.Retry) },
                    modifier = modifier,
                )

            HomeUiState.Empty -> HomeEmpty(modifier)
            is HomeUiState.Content ->
                HomeContent(
                    padding = paddingValues,
                    content = state,
                    onNavigateToSecond = onNavigateToSecond,
                    modifier = modifier,
                )
        }
    }
}

@Composable
private fun HomeLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun HomeError(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(MindeckTheme.dimensions.screenPadding),
        verticalArrangement =
            Arrangement.spacedBy(
                MindeckTheme.dimensions.spacingLg,
                Alignment.CenterVertically,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.background(
                MaterialTheme.colorScheme.errorContainer,
                shape = CircleShape
            )
        ) {
            Icon(
                painter = painterResource(Res.drawable.cloud_off_icon),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(MindeckTheme.dimensions.spacingXxl)
                    .size(MindeckTheme.dimensions.iconLg),
            )
        }
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )
        AppButton(
            onAction = onRetry,
            buttonText = "Повторить загрузку",
            buttonIcon = Res.drawable.refresh_icon,
            textColor = MaterialTheme.colorScheme.onErrorContainer,
            color = MaterialTheme.colorScheme.errorContainer,
        )
    }
}

@Composable
private fun HomeEmpty(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Пока нет колод",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

@Composable
private fun HomeContent(
    padding: PaddingValues,
    content: HomeUiState.Content,
    onNavigateToSecond: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.padding(padding),
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXxxl),
    ) {
        DailyReviewCard(
            dailyReview = content.dailyReview,
            onStartReview = onNavigateToSecond,
            onContinueReview = onNavigateToSecond,
            modifier = Modifier.padding(top = MindeckTheme.dimensions.spacingMd),
        )
        Decks(
            decks = content.decks,
            onAllDecksClick = { },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun DailyReviewCard(
    dailyReview: DailyReviewUi,
    onStartReview: () -> Unit,
    onContinueReview: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MindeckTheme.shapes.hero,
        colors = CardDefaults.cardColors(if (dailyReview !is DailyReviewUi.Completed) MaterialTheme.colorScheme.primaryContainer else MindeckTheme.extraColors.ratingGoodBackground),
    ) {
        Column(
            modifier =
                Modifier.padding(
                    start = MindeckTheme.dimensions.spacingXl,
                    top = MindeckTheme.dimensions.spacingLg,
                    end = MindeckTheme.dimensions.spacingXl,
                    bottom = MindeckTheme.dimensions.spacingLg,
                ),
        ) {
            Text(
                text = if (dailyReview !is DailyReviewUi.Completed) "СЕГОДНЯ К ПОВТОРЕНИЮ" else "ГОТОВО НА СЕГОДНЯ",
                color = if (dailyReview !is DailyReviewUi.Completed) MaterialTheme.colorScheme.primary else MindeckTheme.extraColors.ratingGoodOn.copy(
                    alpha = 0.8f
                ),
                style = MaterialTheme.typography.titleSmall,
            )
            when (dailyReview) {
                is DailyReviewUi.Pending -> PendingReview(
                    dailyReview,
                    onStartReview,
                    onContinueReview
                )

                DailyReviewUi.Completed -> CompletedReview({})
            }
        }
    }
}

@Composable
private fun ColumnScope.PendingReview(
    pending: DailyReviewUi.Pending,
    onStartReview: () -> Unit,
    onContinueReview: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXs),
    ) {
        Text(
            text = (pending.totalCount - pending.repeatedCount).toString(),
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
        TagLabel(MaterialTheme.colorScheme.primary, "${pending.newCount} новых")
        TagLabel(
            MaterialTheme.colorScheme.primary.copy(alpha = .70f),
            "${pending.newReviewCount} новых на повторение",
        )
        TagLabel(
            MaterialTheme.colorScheme.primary.copy(alpha = .60f),
            "${pending.reviewCount} на повторение",
        )
    }

    VerticalSpacer(MindeckTheme.dimensions.spacingLg)

    if (pending.inProgress) {
        RepetitionScaleInfo(
            totalCount = pending.totalCount, repeatedCount = pending.repeatedCount
        )
        VerticalSpacer(MindeckTheme.dimensions.spacingXl)
    }

    if (pending.inProgress) {
        AppButton(
            onAction = onContinueReview,
            buttonText = "Продолжить повторение",
            buttonIcon = Res.drawable.play_arrow_icon,
            color = MaterialTheme.colorScheme.primary,
        )
    } else {
        AppButton(
            onAction = onStartReview,
            buttonText = "Начать повторение",
            buttonIcon = Res.drawable.play_arrow_icon,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun ColumnScope.CompletedReview(
    onStartReview: () -> Unit
) {
    VerticalSpacer(MindeckTheme.dimensions.spacingSm)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXs),
        verticalAlignment = Alignment.Bottom,
    ) {
        Icon(
            painter = painterResource(Res.drawable.check_circle_icon),
            contentDescription = null,
            modifier = Modifier.size(MindeckTheme.dimensions.iconXl),
            tint = MindeckTheme.extraColors.ratingGoodOn.copy(alpha = 0.9f),
        )
        Text(
            text = "повторено",
            color = MindeckTheme.extraColors.ratingGoodOn.copy(alpha = 0.5f),
            style = MaterialTheme.typography.titleMedium,
        )
    }

    VerticalSpacer(MindeckTheme.dimensions.spacingLg)

    AppButton(
        onAction = onStartReview,
        buttonText = "Посмотреть статистику",
        buttonIcon = Res.drawable.bar_chart_icon,
        color = MindeckTheme.extraColors.ratingGoodOn,
    )
}

@Composable
fun RepetitionScaleInfo(totalCount: Int, repeatedCount: Int, modifier: Modifier = Modifier) {
    val progress = if (totalCount > 0) repeatedCount.toFloat() / totalCount else 0f

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXxs)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "$repeatedCount из $totalCount готово",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary.copy(alpha = .5f)
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary.copy(alpha = .5f)
            )
        }
        RepetitionScale(progress)
    }
}

@Composable
fun RepetitionScale(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxWidth().height(MindeckTheme.dimensions.spacingSm)
                .background(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f),
                    shape = MindeckTheme.shapes.avatar
                )
        )
        Box(
            modifier = Modifier.fillMaxWidth(progress).height(MindeckTheme.dimensions.spacingSm)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                    shape = MindeckTheme.shapes.avatar
                )
        )
    }
}

@Composable
private fun Decks(
    decks: List<DeckUi>,
    onAllDecksClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingLg),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                "КОЛОДЫ",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = MindeckTheme.dimensions.spacingSm),
            )
            Row(
                modifier =
                    Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onAllDecksClick() }
                        .padding(end = MindeckTheme.dimensions.spacingLg),
                horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingSm),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.library_decks_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(MindeckTheme.dimensions.iconXs),
                )
                Text(
                    text = "Все",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingSm),
        ) {
            items(decks, key = { it.id }) { deck ->
                DeckItem(deck = deck)
            }
        }
    }
}

@Composable
private fun DeckItem(
    deck: DeckUi,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MindeckTheme.shapes.card,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(MindeckTheme.dimensions.spacingMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingMd),
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(MindeckTheme.dimensions.touchTarget)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.medium,
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = deck.title.firstOrNull()?.toString() ?: "",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXxs),
                ) {
                    Text(
                        text = deck.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .75f),
                    )
                    Text(
                        text = "${deck.cardCount} карточки",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
            if (deck.hasCardsToReview) {
                Box(
                    modifier =
                        Modifier
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.medium,
                            ).size(
                                height = MindeckTheme.dimensions.spacingXxl,
                                width = MindeckTheme.dimensions.touchTarget
                            ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = deck.reviewCount.toString(),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            } else {
                Icon(
                    painter = painterResource(Res.drawable.check_circle_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}
