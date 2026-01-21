package com.mindeck.domain.usecases.notification

import com.mindeck.domain.repository.NotificationRepository
import javax.inject.Inject

class StopNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository,
) {
    operator fun invoke() {
        repository.cancelWork()
    }
}
