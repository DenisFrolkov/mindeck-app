@file:Suppress("ktlint:standard:function-naming")

package com.mindeck.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureOverlay
import com.mindeck.app.navigation.Navigation

@OptIn(ExperimentalDecomposeApi::class)
fun MainViewController(component: IosApplicationComponent) =
    ComposeUIViewController {
        PredictiveBackGestureOverlay(
            backDispatcher = component.backDispatcher,
            backIcon = null,
            modifier = Modifier.fillMaxSize(),
        ) {
            Navigation(component.rootComponent)
        }
    }
