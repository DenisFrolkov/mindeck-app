package com.mimdeck.data.database.entities

import android.icu.text.CaseMap.Fold
import com.mindeck.domain.models.Folder

object Mappers {
    fun Folder.toData(): FolderEntity {
        return FolderEntity(
            this.folderId,
            this.folderName
        )
    }

    fun FolderEntity.toDomain(): Folder {
        return Folder(
            this.folderId,
            this.folderName
        )
    }
}