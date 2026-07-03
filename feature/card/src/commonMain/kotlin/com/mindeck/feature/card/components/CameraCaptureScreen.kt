package com.mindeck.feature.card.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mindeck.core.ui.theme.MindeckTheme
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.add_a_photo_icon
import mindeck_app.core.ui.generated.resources.close_icon
import mindeck_app.feature.card.generated.resources.create_card_camera_capture
import mindeck_app.feature.card.generated.resources.create_card_camera_close
import mindeck_app.feature.card.generated.resources.create_card_camera_permission_denied
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.card.generated.resources.Res as CreateCardRes

@Composable
internal fun CameraCaptureScreen(
    onCaptured: (ByteArray) -> Unit,
    onFailed: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        val permission = rememberCameraPermissionState()
        LaunchedEffect(Unit) {
            if (!permission.isGranted) permission.request()
        }
        val state =
            rememberPeekabooCameraState(onCapture = { bytes ->
                if (bytes != null) onCaptured(bytes) else onFailed()
            })
        Box(
            modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim),
        ) {
            if (permission.isGranted) {
                PeekabooCamera(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                )
                if (state.isCameraReady) {
                    CircleIconButton(
                        icon = Res.drawable.add_a_photo_icon,
                        contentDescription = stringResource(CreateCardRes.string.create_card_camera_capture),
                        onClick = state::capture,
                        iconSize = MindeckTheme.dimensions.iconXl,
                        modifier =
                            Modifier
                                .align(Alignment.BottomCenter)
                                .navigationBarsPadding()
                                .padding(MindeckTheme.dimensions.spacingXxxl),
                    )
                }
                if (state.isCapturing) {
                    CircularProgressIndicator(
                        color = MindeckTheme.extraColors.onScrim,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            } else {
                Text(
                    text = stringResource(CreateCardRes.string.create_card_camera_permission_denied),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MindeckTheme.extraColors.onScrim,
                    modifier = Modifier.padding(MindeckTheme.dimensions.screenPadding),
                )
            }
            CircleIconButton(
                icon = Res.drawable.close_icon,
                contentDescription = stringResource(CreateCardRes.string.create_card_camera_close),
                onClick = onDismiss,
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .statusBarsPadding()
                        .padding(MindeckTheme.dimensions.spacingLg),
            )
        }
    }
}

@Composable
private fun CircleIconButton(
    icon: DrawableResource,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = MindeckTheme.dimensions.iconMd,
) {
    Box(
        modifier =
            modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.45f))
                .clickable(onClick = onClick)
                .padding(MindeckTheme.dimensions.spacingSm),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = MindeckTheme.extraColors.onScrim,
            modifier = Modifier.size(iconSize),
        )
    }
}
