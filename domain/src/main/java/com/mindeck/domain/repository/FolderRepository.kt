package com.mindeck.domain.repository

import com.mindeck.domain.models.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    suspend fun createFolder(folder: Folder)

    suspend fun renameFolder(folderId: Int, newName: String)

    suspend fun deleteFolder(folder: Folder)

    suspend fun getFolderById(folderId: Int): Folder

    fun getAllFolders(): Flow<List<Folder>>
}