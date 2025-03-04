package com.mimdeck.data

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class AppLifecycleObserver(private val context: Context) : DefaultLifecycleObserver {
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        scheduleWork(context)
    }

    private fun scheduleWork(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            2, TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .setRequiresStorageNotLow(true)
                    .build()
            )
            .build()

        WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
            "card_notification_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}
