package com.mimdeck.data.dataSource

import com.mimdeck.data.database.entities.FolderEntity
import kotlinx.coroutines.flow.Flow

interface FolderDataSource {
    suspend fun insertFolder(folderEntity: FolderEntity)
    suspend fun renameFolder(folderId: Int, newName: String)
    suspend fun deleteFolder(folderEntity: FolderEntity)
    suspend fun getFolderById(folderId: Int): FolderEntity
    fun getAllFolders(): Flow<List<FolderEntity>>
}