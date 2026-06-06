package com.mindeck.feature.home.home

import com.mindeck.core.mvi.BaseViewModel

class HomeViewModel :
    BaseViewModel<HomeUiState, HomeIntent>(
        initialState = HomePreviewData.completedContent,
    ) {
    override fun accept(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Retry -> updateState { HomePreviewData.pendingContent }
        }
    }
}
