package com.mimdeck.data.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mimdeck.data.work.NotificationWorker
import com.mindeck.domain.repository.NotificationRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val context: Context,
) : NotificationRepository {

    override fun scheduleWork() {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            1,
            TimeUnit.MINUTES,
        )
            .setInitialDelay(1, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .setRequiresStorageNotLow(true)
                    .build(),
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "CheckCardsWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest,
        )
    }

    override fun cancelWork() {
        WorkManager.getInstance(context).cancelUniqueWork("CheckCardsWork")
    }
}
