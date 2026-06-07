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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.mindeck.core.ui.appBar.AppBar
import com.mindeck.core.ui.appBar.AppBarAction
import com.mindeck.core.ui.button.AppButton
import com.mindeck.core.ui.button.AppTextButton
import com.mindeck.core.ui.label.TagLabel
import com.mindeck.core.ui.spacer.HorizontalSpacer
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.add_icon
import mindeck_app.core.ui.generated.resources.bar_chart_icon
import mindeck_app.core.ui.generated.resources.check_circle_icon
import mindeck_app.core.ui.generated.resources.cloud_off_icon
import mindeck_app.core.ui.generated.resources.download_icon
import mindeck_app.core.ui.generated.resources.library_decks_icon
import mindeck_app.core.ui.generated.resources.lightbulb_icon
import mindeck_app.core.ui.generated.resources.play_arrow_icon
import mindeck_app.core.ui.generated.resources.refresh_icon
import mindeck_app.core.ui.generated.resources.search_icon
import mindeck_app.core.ui.generated.resources.settings_icon
import mindeck_app.core.ui.generated.resources.style_icon
import mindeck_app.feature.home.generated.resources.home_action_search
import mindeck_app.feature.home.generated.resources.home_action_settings
import mindeck_app.feature.home.generated.resources.home_cards_noun
import mindeck_app.feature.home.generated.resources.home_continue_review
import mindeck_app.feature.home.generated.resources.home_deck_all_reviewed
import mindeck_app.feature.home.generated.resources.home_deck_card_count
import mindeck_app.feature.home.generated.resources.home_decks_all
import mindeck_app.feature.home.generated.resources.home_decks_header
import mindeck_app.feature.home.generated.resources.home_empty_create_card
import mindeck_app.feature.home.generated.resources.home_empty_hint
import mindeck_app.feature.home.generated.resources.home_empty_import_deck
import mindeck_app.feature.home.generated.resources.home_empty_subtitle
import mindeck_app.feature.home.generated.resources.home_empty_title
import mindeck_app.feature.home.generated.resources.home_error_retry
import mindeck_app.feature.home.generated.resources.home_progress_percent
import mindeck_app.feature.home.generated.resources.home_review_completed_label
import mindeck_app.feature.home.generated.resources.home_review_completed_title
import mindeck_app.feature.home.generated.resources.home_review_pending_title
import mindeck_app.feature.home.generated.resources.home_review_progress
import mindeck_app.feature.home.generated.resources.home_start_review
import mindeck_app.feature.home.generated.resources.home_tag_new
import mindeck_app.feature.home.generated.resources.home_tag_new_review
import mindeck_app.feature.home.generated.resources.home_tag_review
import mindeck_app.feature.home.generated.resources.home_title
import mindeck_app.feature.home.generated.resources.home_view_statistics
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.home.generated.resources.Res as HomeRes

@Composable
fun HomeScreen(
    state: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    onNavigateToSecond: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(MindeckTheme.dimensions.screenPadding),
    ) {
        AppBar(
            title = stringResource(HomeRes.string.home_title),
            actions =
                listOf(
                    AppBarAction(
                        icon = Res.drawable.search_icon,
                        contentDescription = stringResource(HomeRes.string.home_action_search),
                        onClick = { },
                    ),
                    AppBarAction(
                        icon = Res.drawable.settings_icon,
                        contentDescription = stringResource(HomeRes.string.home_action_settings),
                        onClick = { },
                    ),
                ),
            modifier = Modifier.statusBarsPadding(),
        )
        when (state) {
            HomeUiState.Loading -> HomeLoading(Modifier.weight(1f))
            is HomeUiState.Error ->
                HomeError(
                    message = state.message,
                    onRetry = { onIntent(HomeIntent.Retry) },
                    modifier = Modifier.weight(1f),
                )

            HomeUiState.Empty -> HomeEmpty(Modifier.weight(1f))
            is HomeUiState.Content ->
                HomeContent(
                    content = state,
                    onNavigateToSecond = onNavigateToSecond,
                    modifier = Modifier.weight(1f),
                )
        }
    }
}

@Composable
private fun HomeLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
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
                .fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                MindeckTheme.dimensions.spacingLg,
                Alignment.CenterVertically,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier.background(
                    MaterialTheme.colorScheme.errorContainer,
                    shape = CircleShape,
                ),
        ) {
            Icon(
                painter = painterResource(Res.drawable.cloud_off_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier =
                    Modifier
                        .padding(MindeckTheme.dimensions.spacingXxl)
                        .size(MindeckTheme.dimensions.iconLg),
            )
        }
        Text(
            text = message,
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                ),
            color = MaterialTheme.colorScheme.onSurface,
        )
        AppButton(
            onAction = onRetry,
            buttonText = stringResource(HomeRes.string.home_error_retry),
            buttonIcon = Res.drawable.refresh_icon,
            textColor = MaterialTheme.colorScheme.onErrorContainer,
            color = MaterialTheme.colorScheme.errorContainer,
        )
    }
}

@Composable
private fun HomeEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier.background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                    ),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.style_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier =
                        Modifier
                            .padding(MindeckTheme.dimensions.spacingXxl)
                            .size(MindeckTheme.dimensions.iconXl),
                )
            }
            VerticalSpacer(MindeckTheme.dimensions.spacingXxxl)
            Text(
                text = stringResource(HomeRes.string.home_empty_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
            )
            VerticalSpacer(MindeckTheme.dimensions.spacingSm)
            Text(
                text = stringResource(HomeRes.string.home_empty_subtitle),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = MindeckTheme.dimensions.spacingXxxl),
            )
            VerticalSpacer(MindeckTheme.dimensions.spacingXxxl)
            AppButton(
                onAction = { },
                buttonText = stringResource(HomeRes.string.home_empty_create_card),
                buttonIcon = Res.drawable.add_icon,
                color = MaterialTheme.colorScheme.primary,
            )
            VerticalSpacer(MindeckTheme.dimensions.spacingXs)
            AppTextButton(
                onAction = { },
                buttonText = stringResource(HomeRes.string.home_empty_import_deck),
                buttonIcon = Res.drawable.download_icon,
            )
        }

        Card(
            shape = MindeckTheme.shapes.card,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)),
        ) {
            Row(
                modifier =
                    Modifier.padding(
                        horizontal = MindeckTheme.dimensions.spacingLg,
                        vertical = MindeckTheme.dimensions.spacingMd,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.lightbulb_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
                Text(
                    text = stringResource(HomeRes.string.home_empty_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f),
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    content: HomeUiState.Content,
    onNavigateToSecond: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
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
        colors =
            CardDefaults.cardColors(
                if (dailyReview !is DailyReviewUi.Completed) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MindeckTheme.extraColors.ratingGoodBackground
                },
            ),
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
                text =
                    if (dailyReview !is DailyReviewUi.Completed) {
                        stringResource(HomeRes.string.home_review_pending_title)
                    } else {
                        stringResource(HomeRes.string.home_review_completed_title)
                    },
                color =
                    if (dailyReview !is DailyReviewUi.Completed) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MindeckTheme.extraColors.ratingGoodOn.copy(
                            alpha = 0.8f,
                        )
                    },
                style = MaterialTheme.typography.titleSmall,
            )
            when (dailyReview) {
                is DailyReviewUi.Pending ->
                    PendingReview(
                        dailyReview,
                        onStartReview,
                        onContinueReview,
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
    val remaining = pending.totalCount - pending.repeatedCount

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXs),
    ) {
        Text(
            text = remaining.toString(),
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
            text = pluralStringResource(HomeRes.plurals.home_cards_noun, remaining),
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
        TagLabel(
            pluralStringResource(HomeRes.plurals.home_tag_new, pending.newCount, pending.newCount),
            MaterialTheme.colorScheme.primary,
        )
        TagLabel(
            pluralStringResource(
                HomeRes.plurals.home_tag_new_review,
                pending.newReviewCount,
                pending.newReviewCount,
            ),
            MaterialTheme.colorScheme.primary.copy(alpha = .70f),
        )
        TagLabel(
            stringResource(HomeRes.string.home_tag_review, pending.reviewCount),
            MaterialTheme.colorScheme.primary.copy(alpha = .60f),
        )
    }

    VerticalSpacer(MindeckTheme.dimensions.spacingLg)

    if (pending.inProgress) {
        RepetitionScaleInfo(
            totalCount = pending.totalCount,
            repeatedCount = pending.repeatedCount,
        )
        VerticalSpacer(MindeckTheme.dimensions.spacingXl)
    }

    if (pending.inProgress) {
        AppButton(
            onAction = onContinueReview,
            buttonText = stringResource(HomeRes.string.home_continue_review),
            buttonIcon = Res.drawable.play_arrow_icon,
            color = MaterialTheme.colorScheme.primary,
        )
    } else {
        AppButton(
            onAction = onStartReview,
            buttonText = stringResource(HomeRes.string.home_start_review),
            buttonIcon = Res.drawable.play_arrow_icon,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun ColumnScope.CompletedReview(onStartReview: () -> Unit) {
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
            text = stringResource(HomeRes.string.home_review_completed_label),
            color = MindeckTheme.extraColors.ratingGoodOn.copy(alpha = 0.5f),
            style = MaterialTheme.typography.titleMedium,
        )
    }

    VerticalSpacer(MindeckTheme.dimensions.spacingLg)

    AppButton(
        onAction = onStartReview,
        buttonText = stringResource(HomeRes.string.home_view_statistics),
        buttonIcon = Res.drawable.bar_chart_icon,
        color = MindeckTheme.extraColors.ratingGoodOn,
    )
}

@Composable
private fun RepetitionScaleInfo(
    totalCount: Int,
    repeatedCount: Int,
    modifier: Modifier = Modifier,
) {
    val progress = if (totalCount > 0) repeatedCount.toFloat() / totalCount else 0f

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXxs),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text =
                    stringResource(
                        HomeRes.string.home_review_progress,
                        repeatedCount,
                        totalCount,
                    ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary.copy(alpha = .5f),
            )
            Text(
                text =
                    stringResource(
                        HomeRes.string.home_progress_percent,
                        (progress * 100).toInt(),
                    ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary.copy(alpha = .5f),
            )
        }
        RepetitionScale(progress)
    }
}

@Composable
private fun RepetitionScale(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(MindeckTheme.dimensions.spacingSm)
                    .background(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f),
                        shape = MindeckTheme.shapes.avatar,
                    ),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(progress)
                    .height(MindeckTheme.dimensions.spacingSm)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                        shape = MindeckTheme.shapes.avatar,
                    ),
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
                stringResource(HomeRes.string.home_decks_header),
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
                    text = stringResource(HomeRes.string.home_decks_all),
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
                        text =
                            pluralStringResource(
                                HomeRes.plurals.home_deck_card_count,
                                deck.cardCount,
                                deck.cardCount,
                            ),
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
                                width = MindeckTheme.dimensions.touchTarget,
                            ),
                    contentAlignment = Alignment.Center,
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
                    contentDescription = stringResource(HomeRes.string.home_deck_all_reviewed),
                    tint = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}
