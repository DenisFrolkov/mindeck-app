package com.mindeck.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.mindeck.domain.models.Folder
import com.mindeck.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Test(vm)
//            AppNavigation()
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Test(viewModel: MainViewModel) {

    val res = viewModel.result.collectAsState().value

    val scope = CoroutineScope(Dispatchers.IO)

    viewModel.getFolder()

    LazyColumn {
        item {
            Button(
                onClick = {
                    scope.launch {
                        viewModel.create(Folder(folderName = "87"))
                    }
                }
            ) { }
        }
        items(res) {
            Text(text = it.folderName)
        }
    }
}
