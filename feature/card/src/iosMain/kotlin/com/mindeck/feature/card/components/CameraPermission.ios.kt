package com.mindeck.feature.card.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberCameraPermissionState(): CameraPermissionState =
    remember {
        object : CameraPermissionState {
            override val isGranted = true

            override fun request() = Unit
        }
    }
