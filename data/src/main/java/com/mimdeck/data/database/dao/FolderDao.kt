package com.mimdeck.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mimdeck.data.database.entities.Folder
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {

    @Insert()
    suspend fun insertFolder(folder: Folder)

    @Query("UPDATE folder SET folder_name = :newName WHERE folder_id = :folderId")
    suspend fun renameFolder(folderId: Int, newName: String)

    @Delete
    suspend fun deleteFolder(folder: Folder)

    @Query("SELECT * FROM folder")
    fun getAllFolders(): Flow<List<Folder>>
}