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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.ui.components.fab.FAB
import com.mindeck.presentation.ui.components.fab.FabMenuDataClass
import com.mindeck.presentation.ui.components.fab.FabState
import com.mindeck.presentation.ui.components.fab.FabState.Companion.ITEM_HEIGHT
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.White

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val item = listOf(
                FabMenuDataClass(
                    0,
                    "1",
                    painterResource(com.mindeck.presentation.R.drawable.fab_open_menu_setting_icon)
                ),
                FabMenuDataClass(
                    1,
                    "1",
                    painterResource(com.mindeck.presentation.R.drawable.fab_open_menu_setting_icon)
                ),
                FabMenuDataClass(
                    2,
                    "1",
                    painterResource(com.mindeck.presentation.R.drawable.fab_open_menu_setting_icon)
                ),
                FabMenuDataClass(
                    3,
                    "1",
                    painterResource(com.mindeck.presentation.R.drawable.fab_open_menu_setting_icon)
                )
            )

            val fabState = remember { FabState(expandedHeight = (ITEM_HEIGHT * item.size).dp) }

            Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxSize()) {
                Column() {
                    Button(onClick = {
                        if (fabState.isExpanded == true) {
                            fabState.reset()
                        }
                    }) { }
                    FAB(
                        fabColor = Blue,
                        fabIconColor = White,
                        fabIcon = painterResource(com.mindeck.presentation.R.drawable.fab_menu_icon),
                        fabMenuItems = item,
                        fabState = fabState
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}
