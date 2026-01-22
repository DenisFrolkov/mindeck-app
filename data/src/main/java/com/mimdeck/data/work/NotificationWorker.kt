package com.mimdeck.data.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mimdeck.data.database.dao.CardDao
import com.mindeck.data.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    private val cardDao: CardDao,
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            val currentTime = System.currentTimeMillis()
            val card = cardDao.getCardsRepetition(currentTime)
                .firstOrNull() ?: emptyList()

            if (card.isNotEmpty()) {
                sendNotification(card.size)
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun sendNotification(count: Int) {
        val channelId = "card_repetition_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Повторение карточек",
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = "Уведомления о карточках, которые пора повторить"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Пора повторить карточки!")
            .setContentText("Стоит повторить $count карточек!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
