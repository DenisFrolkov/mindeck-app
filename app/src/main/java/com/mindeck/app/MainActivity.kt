package com.mindeck.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.mindeck.presentation.ui.navigation.AppNavigation
import com.mindeck.presentation.viewmodel.FolderViewModel
import com.mindeck.presentation.viewmodel.FoldersViewModel
import com.mindeck.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val foldersViewModel: FoldersViewModel by viewModels()
    private val folderViewModel: FolderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation(
                mainViewModel = mainViewModel,
                foldersViewModel = foldersViewModel,
                folderViewModel = folderViewModel
            )
        }
    }
}

