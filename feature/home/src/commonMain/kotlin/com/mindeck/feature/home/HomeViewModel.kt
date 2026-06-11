package com.mindeck.feature.home

import com.mindeck.core.mvi.BaseViewModel
import com.mindeck.domain.usecases.deck.query.GetDecksWithStatsUseCase
import com.mindeck.feature.home.model.DailyReviewUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Clock

class HomeViewModel(
    private val getDecksWithStatsUseCase: GetDecksWithStatsUseCase,
) : BaseViewModel<HomeUiState, HomeIntent>(HomeUiState.Loading) {
    override fun accept(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Retry -> {
                sessionTotalCount = null
                getDecksWithStats(Clock.System.now().toEpochMilliseconds())
            }
        }
    }

    init {
        getDecksWithStats(Clock.System.now().toEpochMilliseconds())
    }

    private var statsJob: Job? = null
    private var sessionTotalCount: Int? = null

    private fun getDecksWithStats(currentTime: Long) {
        updateState { HomeUiState.Loading }

        statsJob?.cancel()
        statsJob =
            getDecksWithStatsUseCase(currentTime)
                .onEach { deckWithStats ->
                    val currentDue = deckWithStats.sumOf { it.newCount + it.newReviewCount + it.reviewCount }
                    val total = sessionTotalCount ?: currentDue.also { sessionTotalCount = it }
                    updateState {
                        if (deckWithStats.isEmpty()) {
                            HomeUiState.Empty
                        } else {
                            HomeUiState.Content(
                                dailyReview =
                                    DailyReviewUi.Pending(
                                        totalCount = total,
                                        reviewedCount = (total - currentDue).coerceAtLeast(0),
                                        newCount = deckWithStats.sumOf { it.newCount },
                                        newReviewCount = deckWithStats.sumOf { it.newReviewCount },
                                        reviewCount = deckWithStats.sumOf { it.reviewCount },
                                    ),
                                decks = deckWithStats.map { it.toUi() },
                            )
                        }
                    }
                }.catch { error ->
                    updateState { HomeUiState.Error(error.message ?: "Error") }
                }.launchIn(viewModelScope)
    }
}
