package com.mindeck.feature.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mindeck.core.ui.theme.MindeckTheme

@Composable
fun CreateCardScreen(
    state: CreateCardUiState,
    onIntent: (CreateCardIntent) -> Unit,
    onNavigate: (CreateCardNavigationEvent) -> Unit,
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

    }
}