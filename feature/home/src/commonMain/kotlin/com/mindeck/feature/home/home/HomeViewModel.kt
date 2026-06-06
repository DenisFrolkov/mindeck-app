package com.mindeck.feature.home.home

import com.mindeck.core.mvi.BaseViewModel

class HomeViewModel :
    BaseViewModel<HomeUiState, HomeIntent>(
        initialState = HomePreviewData.error,
    ) {
    override fun accept(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Retry -> updateState { HomePreviewData.pendingContent }
        }
    }
}
