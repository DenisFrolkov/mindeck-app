package com.mimdeck.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "folder", indices = [Index(value = ["folder_name"], unique = true)])
data class FolderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "folder_id")
    val folderId: Int = 0,
    @ColumnInfo(name = "folder_name") val folderName: String?,
)
