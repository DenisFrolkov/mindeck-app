package com.mindeck.presentation.ui.navigation

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.mindeck.presentation.ui.screens.CardScreen
import com.mindeck.presentation.ui.screens.CardStudyScreen
import com.mindeck.presentation.ui.screens.CreationCardScreen
import com.mindeck.presentation.ui.screens.DeckScreen
import com.mindeck.presentation.ui.screens.DecksScreen
import com.mindeck.presentation.ui.screens.MainScreen

@Composable
fun MyApp() {
    val context = LocalContext.current
    val backStack = remember { mutableStateListOf<NavigationRoute>(MainRoute) }
    val navigator = remember { StackNavigator(backStack) }
    val activity = context as? Activity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        CompositionLocalProvider(LocalNavigator provides navigator) {
            val nav = LocalNavigator.current

            NavDisplay(
                backStack = backStack,
                onBack = {
                    if (backStack.size <= 1) {
                        activity?.finish()
                    } else {
                        nav.pop()
                    }
                },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider {
                    entry<MainRoute> {
                        MainScreen()
                    }
                    entry<CreationCardRoute> { route ->
                        CreationCardScreen(route.deckId)
                    }
                    entry<DecksRoute> {
                        DecksScreen()
                    }
                    entry<DeckRoute> { route ->
                        DeckScreen(route.deckId)
                    }
                    entry<CardRoute> { route ->
                        CardScreen(route.cardId)
                    }
                    entry<CardStudyRoute> {
                        CardStudyScreen()
                    }
                },
            )
        }
    }
}
