package com.mindeck.domain.usecases.notificationUseCase

import com.mindeck.domain.repository.NotificationRepository

class StopNotificationUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke() {
        repository.cancelWork()
    }
}