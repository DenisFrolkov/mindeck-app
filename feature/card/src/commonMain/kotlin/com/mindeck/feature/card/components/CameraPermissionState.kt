package com.mindeck.feature.card.components

import androidx.compose.runtime.Composable

/**
 * Peekaboo's own Android permission flow only calls `launchPermissionRequest()` once
 * `shouldShowRationale` is true, which Android never sets on the very first ask — so the stock
 * flow silently falls through to its "denied" content without ever prompting. We request the
 * permission ourselves before showing the camera so Peekaboo always sees an already-resolved
 * state. iOS needs no such workaround: Peekaboo's iOS side already requests access correctly.
 */
internal interface CameraPermissionState {
    val isGranted: Boolean

    fun request()
}

@Composable
internal expect fun rememberCameraPermissionState(): CameraPermissionState
