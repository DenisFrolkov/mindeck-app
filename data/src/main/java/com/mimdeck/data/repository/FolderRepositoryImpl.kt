package com.mimdeck.data.repository

import android.database.SQLException
import android.util.Log
import com.mimdeck.data.dataSource.DatabaseException
import com.mimdeck.data.dataSource.FolderDataSource
import com.mimdeck.data.database.entities.Mappers.toData
import com.mimdeck.data.database.entities.Mappers.toDomain
import com.mindeck.domain.exception.DomainException
import com.mindeck.domain.models.Folder
import com.mindeck.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FolderRepositoryImpl(private val localDataSource: FolderDataSource) : FolderRepository {
    override suspend fun createFolder(folder: Folder) {
        try {
            localDataSource.insertFolder(folderEntity = folder.toData())
        } catch (e: DatabaseException) {
            throw DomainException("Failed to create folder: ${e.localizedMessage}", e)
        }

    }

    override suspend fun renameFolder(folderId: Int, newName: String) {
        try {
            localDataSource.renameFolder(folderId = folderId, newName = newName)
        } catch (e: DatabaseException) {
            throw DomainException("Failed to renamed folder: ${e.localizedMessage}", e)
        }
    }

    override suspend fun deleteFolder(folder: Folder) {
        try {
            localDataSource.deleteFolder(folderEntity = folder.toData())
        } catch (e: DatabaseException) {
            throw DomainException("Failed to delete folder: ${e.localizedMessage}", e)
        }
    }

    override fun getAllFolders(): Flow<List<Folder>> {
        return try {
            localDataSource.getAllFolders()
                .map { folderEntityList -> folderEntityList.map { it.toDomain() } }
        } catch (e: DatabaseException) {
            flow { emit(emptyList<Folder>()) }
            throw DomainException("Failed get all folders", e)
        }
    }
}