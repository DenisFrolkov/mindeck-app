package com.mimdeck.data.dataSource.dataSourceImpl

import android.database.SQLException
import com.mimdeck.data.dataSource.DatabaseException
import com.mimdeck.data.dataSource.FolderDataSource
import com.mimdeck.data.database.dao.FolderDao
import com.mimdeck.data.database.entities.FolderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FolderLocalDataSourceImpl(
    private val folderDao: FolderDao
) : FolderDataSource {

    override suspend fun insertFolder(folderEntity: FolderEntity) {
        try {
            folderDao.insertFolder(folderEntity)
        } catch (e: SQLException) {
            throw DatabaseException("Failed to insert folder due to a constraint violation: ${e.localizedMessage}", e)
        } catch (e: Exception) {
            throw DatabaseException("Unknown database error occurred", e)
        }
    }

    override suspend fun renameFolder(folderId: Int, newName: String) {
        try {
            folderDao.renameFolder(folderId, newName)
        } catch (e: SQLException) {
            throw DatabaseException("Failed to insert folder due to a constraint violation: ${e.localizedMessage}", e)
        } catch (e: Exception) {
            throw DatabaseException("Error renaming folder: ${e.localizedMessage}", e)
        }
    }

    override suspend fun deleteFolder(folderEntity: FolderEntity) {
        try {
            folderDao.deleteFolder(folderEntity)
        } catch (e: SQLException) {
            throw DatabaseException("Failed to insert folder due to a constraint violation: ${e.localizedMessage}", e)
        } catch (e: Exception) {
            throw DatabaseException("Error deleting folder: ${e.localizedMessage}", e)
        }
    }

    override fun getAllFolders(): Flow<List<FolderEntity>> {
        return try {
            folderDao.getAllFolders()
        } catch (e: SQLException) {
            throw DatabaseException("Failed to insert folder due to a constraint violation: ${e.localizedMessage}", e)
        } catch (e: Exception) {
            flow {
                emit(emptyList<FolderEntity>())
            }
            throw DatabaseException("Error getting a list of folders: ${e.localizedMessage}", e)
        }
    }
}