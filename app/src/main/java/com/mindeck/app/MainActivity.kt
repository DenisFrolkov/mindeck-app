package com.mindeck.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mindeck.presentation.ui.navigation.MyApp
import com.mindeck.presentation.ui.theme.MindeckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindeckTheme {
                MyApp()
            }
        }
    }
}
