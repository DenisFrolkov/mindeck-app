package com.mindeck.domain.repository

interface NotificationRepository {
    fun scheduleWork()

    fun cancelWork()
}
