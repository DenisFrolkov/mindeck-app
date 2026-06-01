package com.mindeck.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mindeck.presentation.ui.navigation.LocalRootComponent

@Composable
fun SecondScreen() {
    val root = LocalRootComponent.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Second Screen")
        Button(onClick = { root.pop() }) {
            Text(text = "Go back")
        }
    }
}