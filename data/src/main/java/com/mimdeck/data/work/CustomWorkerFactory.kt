package com.mimdeck.data.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.mimdeck.data.database.dao.CardDao
import javax.inject.Inject

class CustomWorkerFactory @Inject constructor(private val cardDao: CardDao) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = NotificationWorker(cardDao, appContext, workerParameters)
}