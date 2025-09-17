package com.mindeck.presentation.viewmodel

import com.mindeck.domain.usecases.notification.StartNotificationUseCase
import com.mindeck.domain.usecases.notification.StopNotificationUseCase
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.viewmodel.managers.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val startNotificationUseCase: StartNotificationUseCase,
    private val stopNotificationUseCase: StopNotificationUseCase
): BaseViewModel() {

    private val _notificationState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val notificationState = _notificationState.asStateFlow()

    fun startNotifications() = launchUiState(_notificationState) {
        startNotificationUseCase()
    }

    fun stopNotifications() = launchUiState(_notificationState) {
        stopNotificationUseCase()
    }
}
