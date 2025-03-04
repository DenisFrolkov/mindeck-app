package com.mindeck.app

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mimdeck.data.NotificationWorker
import com.mindeck.presentation.ui.navigation.AppNavigation
import com.mindeck.presentation.ui.theme.MindeckTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import android.Manifest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindeckTheme() {
                if (!isNotificationPermissionGranted()) {
                    requestNotificationPermission()
                }
                AppNavigation()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        scheduleWork(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        cancelPeriodicWork(applicationContext)
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1
            )
        }
    }

}

private fun cancelPeriodicWork(context: Context) {
    WorkManager.getInstance(context).cancelUniqueWork("card_notification_work")
}


private fun scheduleWork(context: Context) {
    val periodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
        2, TimeUnit.MINUTES
    )
        .setInitialDelay(2, TimeUnit.MINUTES)
        .setConstraints(
            Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build()
        )
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "CheckCardsWork",
        ExistingPeriodicWorkPolicy.REPLACE,
        periodicWorkRequest
    )
}


