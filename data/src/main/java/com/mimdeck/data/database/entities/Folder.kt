package com.mimdeck.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "folders", indices = [Index(value = ["folder_name"], unique = true)])
data class Folder(
    @PrimaryKey
    @ColumnInfo(name = "folder_id")
    val folderId: Int,
    @ColumnInfo(name = "folder_name") val folderName: String,
    @ColumnInfo(name = "creation_date") val creationDate: Long
)
