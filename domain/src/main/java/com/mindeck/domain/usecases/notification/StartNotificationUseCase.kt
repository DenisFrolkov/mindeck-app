package com.mindeck.domain.usecases.notification

import com.mindeck.domain.repository.NotificationRepository
import javax.inject.Inject

class StartNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository,
) {
    operator fun invoke() {
        repository.scheduleWork()
    }
}
