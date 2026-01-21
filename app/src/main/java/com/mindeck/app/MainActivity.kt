package com.mindeck.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.mindeck.app.utils.isNotificationPermissionGranted
import com.mindeck.app.utils.requestNotificationPermission
import com.mindeck.presentation.ui.navigation.AppNavigation
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.viewmodel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindeckTheme() {
                if (!isNotificationPermissionGranted(this)) {
                    requestNotificationPermission(this)
                }
                AppNavigation()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        appViewModel.startNotifications()
    }

    override fun onStart() {
        super.onStart()
        appViewModel.stopNotifications()
    }
}
