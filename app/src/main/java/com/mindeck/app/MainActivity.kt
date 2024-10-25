package com.mindeck.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.ui.components.FAB
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.R.drawable.fab_menu_icon

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var click by remember { mutableStateOf(false) }
            MindeckTheme {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        FAB(
                            fabColor = MaterialTheme.colorScheme.primary,
                            fabIconColor = MaterialTheme.colorScheme.onPrimary,
                            fabShape = 50F,
                            painterResource(fab_menu_icon),
                            click = click,
                            onClick = {
                                click = !click
                            }
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}