package com.mindeck.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import com.mindeck.core.ui.button.AppButton
import com.mindeck.core.ui.label.TagLabel
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.home.model.DailyReviewUi
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.bar_chart_icon
import mindeck_app.core.ui.generated.resources.check_circle_icon
import mindeck_app.core.ui.generated.resources.play_arrow_icon
import mindeck_app.feature.home.generated.resources.home_cards_noun
import mindeck_app.feature.home.generated.resources.home_continue_review
import mindeck_app.feature.home.generated.resources.home_progress_percent
import mindeck_app.feature.home.generated.resources.home_review_completed_label
import mindeck_app.feature.home.generated.resources.home_review_completed_title
import mindeck_app.feature.home.generated.resources.home_review_pending_title
import mindeck_app.feature.home.generated.resources.home_review_progress
import mindeck_app.feature.home.generated.resources.home_start_review
import mindeck_app.feature.home.generated.resources.home_tag_new
import mindeck_app.feature.home.generated.resources.home_tag_new_review
import mindeck_app.feature.home.generated.resources.home_tag_review
import mindeck_app.feature.home.generated.resources.home_view_statistics
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.home.generated.resources.Res as HomeRes

@Composable
internal fun DailyReviewCard(
    dailyReview: DailyReviewUi,
    onReview: () -> Unit,
    onViewStatistics: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MindeckTheme.shapes.hero,
        colors =
            CardDefaults.cardColors(
                if (dailyReview !is DailyReviewUi.Completed)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MindeckTheme.extraColors.ratingGoodBackground
                ,
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
                    if (dailyReview !is DailyReviewUi.Completed)
                        stringResource(HomeRes.string.home_review_pending_title)
                    else
                        stringResource(HomeRes.string.home_review_completed_title),
                color =
                    if (dailyReview !is DailyReviewUi.Completed)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MindeckTheme.extraColors.ratingGoodOn.copy(alpha = 0.8f),
                style = MaterialTheme.typography.titleSmall,
            )
            when (dailyReview) {
                is DailyReviewUi.Pending ->
                    PendingReview(
                        pending = dailyReview,
                        onReview = onReview,
                    )

                DailyReviewUi.Completed -> CompletedReview(onViewStatistics = onViewStatistics)
            }
        }
    }
}

@Composable
private fun ColumnScope.PendingReview(
    pending: DailyReviewUi.Pending,
    onReview: () -> Unit,
) {
    val remaining = pending.totalCount - pending.reviewedCount

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
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
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
            MaterialTheme.colorScheme.onPrimaryContainer,
        )
        TagLabel(
            pluralStringResource(
                HomeRes.plurals.home_tag_new_review,
                pending.newReviewCount,
                pending.newReviewCount,
            ),
            MaterialTheme.colorScheme.primary.copy(alpha = .75f),
        )
        TagLabel(
            stringResource(HomeRes.string.home_tag_review, pending.reviewCount),
            MaterialTheme.colorScheme.primary.copy(alpha = .65f),
        )
    }

    VerticalSpacer(MindeckTheme.dimensions.spacingLg)

    if (pending.inProgress) {
        RepetitionScaleInfo(
            totalCount = pending.totalCount,
            reviewedCount = pending.reviewedCount,
        )
        VerticalSpacer(MindeckTheme.dimensions.spacingXl)
    }

    if (pending.inProgress) {
        AppButton(
            onAction = onReview,
            buttonText = stringResource(HomeRes.string.home_continue_review),
            buttonIcon = Res.drawable.play_arrow_icon,
            color = MaterialTheme.colorScheme.primary,
        )
    } else {
        AppButton(
            onAction = onReview,
            buttonText = stringResource(HomeRes.string.home_start_review),
            buttonIcon = Res.drawable.play_arrow_icon,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun ColumnScope.CompletedReview(onViewStatistics: () -> Unit) {
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
            color = MindeckTheme.extraColors.ratingGoodOn.copy(alpha = 0.8f),
            style = MaterialTheme.typography.titleMedium,
        )
    }

    VerticalSpacer(MindeckTheme.dimensions.spacingLg)

    AppButton(
        onAction = onViewStatistics,
        buttonText = stringResource(HomeRes.string.home_view_statistics),
        buttonIcon = Res.drawable.bar_chart_icon,
        color = MindeckTheme.extraColors.ratingGoodOn,
        textColor = MindeckTheme.extraColors.ratingGoodBackground,
    )
}

@Composable
private fun RepetitionScaleInfo(
    totalCount: Int,
    reviewedCount: Int,
    modifier: Modifier = Modifier,
) {
    val progress = if (totalCount > 0) reviewedCount.toFloat() / totalCount else 0f

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXxs),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text =
                    stringResource(
                        HomeRes.string.home_review_progress,
                        reviewedCount,
                        totalCount,
                    ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text =
                    stringResource(
                        HomeRes.string.home_progress_percent,
                        (progress * 100).toInt(),
                    ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = MindeckTheme.shapes.avatar,
                    ),
        )
    }
}
