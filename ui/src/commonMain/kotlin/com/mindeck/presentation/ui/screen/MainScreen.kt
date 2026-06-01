package com.mindeck.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mindeck.presentation.ui.navigation.Config
import com.mindeck.presentation.ui.navigation.LocalRootComponent

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val root = LocalRootComponent.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Main Screen")
        Button(onClick = { root.push(Config.Second) }) {
            Text(text = "Go to Second")
        }
    }
}
