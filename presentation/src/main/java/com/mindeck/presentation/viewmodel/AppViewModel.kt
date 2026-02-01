package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.mindeck.domain.usecases.notification.StartNotificationUseCase
import com.mindeck.domain.usecases.notification.StopNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val startNotificationUseCase: StartNotificationUseCase,
    private val stopNotificationUseCase: StopNotificationUseCase,
) : ViewModel() {

    fun startNotifications() {
        startNotificationUseCase.invoke()
    }

    fun stopNotifications() {
        stopNotificationUseCase.invoke()
    }
}
