package com.mindeck.feature.home

import com.mindeck.core.mvi.BaseViewModel
import com.mindeck.domain.dispatchers.AppDispatchers
import com.mindeck.domain.models.DailyProgress
import com.mindeck.domain.models.DeckWithStats
import com.mindeck.domain.usecases.deck.query.GetDecksWithStatsUseCase
import com.mindeck.feature.home.model.DailyReviewUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeViewModel(
    private val getDecksWithStatsUseCase: GetDecksWithStatsUseCase,
    dispatchers: AppDispatchers,
    initialState: HomeUiState = HomeUiState.Loading,
) : BaseViewModel<HomeUiState, HomeIntent, Nothing>(initialState, dispatchers.main) {
    override fun accept(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Refresh -> observeDecksWithStats()
        }
    }

    private var statsJob: Job? = null

    private val hasData: Boolean
        get() = currentState is HomeUiState.Content || currentState is HomeUiState.Empty

    private fun observeDecksWithStats() {
        if (!hasData) updateState { HomeUiState.Loading }

        statsJob?.cancel()
        statsJob =
            getDecksWithStatsUseCase()
                .onEach { decksWithStats -> updateState { decksWithStats.toUiState() } }
                .catch { error -> updateState { HomeUiState.Error(error.message ?: "Error") } }
                .launchIn(viewModelScope)
    }

    private fun List<DeckWithStats>.toUiState(): HomeUiState {
        if (isEmpty()) return HomeUiState.Empty

        val progress =
            DailyProgress(
                reviewedCount = sumOf { it.reviewedTodayCount },
                dueCount = sumOf { it.newCount + it.newReviewCount + it.reviewCount },
            )
        return HomeUiState.Content(
            dailyReview =
                DailyReviewUi.Pending(
                    totalCount = progress.totalCount,
                    reviewedCount = progress.reviewedCount,
                    newCount = sumOf { it.newCount },
                    newReviewCount = sumOf { it.newReviewCount },
                    reviewCount = sumOf { it.reviewCount },
                ),
            decks = map { it.toUi() },
        )
    }
}
