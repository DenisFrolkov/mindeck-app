package com.mindeck.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.DefaultComponentContext
import com.mindeck.presentation.ui.navigation.Navigation
import com.mindeck.presentation.ui.navigation.RootComponent
import com.mindeck.presentation.ui.theme.MindeckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val rootComponent = RootComponent(
            componentContext = DefaultComponentContext(lifecycle)
        )

        setContent {
            MindeckTheme {
                Navigation(rootComponent)
            }
        }
    }
}
