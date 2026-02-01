package com.mindeck.app.utils

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.core.app.ActivityCompat

fun requestNotificationPermission(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            1,
        )
    }
}
