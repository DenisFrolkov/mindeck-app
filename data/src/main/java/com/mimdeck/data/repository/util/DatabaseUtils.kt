package com.mimdeck.data.repository.util

import android.database.sqlite.SQLiteConstraintException
import com.mimdeck.data.exception.DatabaseException
import com.mindeck.domain.exception.DomainError

suspend inline fun <T> handleDatabaseSuspend(crossinline block: suspend () -> T): T =
    try {
        block()
    } catch (e: SQLiteConstraintException) {
        throw DomainError.NameAlreadyExists(e.message ?: "Unknown error")
    } catch (e: DatabaseException) {
        throw DomainError.DatabaseError(e.message ?: "Unknown error")
    } catch (e: Exception) {
        throw DomainError.UnknownError(e.message ?: "Unknown error")
    }

inline fun <T> handleDatabase(block: () -> T): T =
    try {
        block()
    } catch (e: SQLiteConstraintException) {
        throw DomainError.NameAlreadyExists(e.message ?: "Unknown error")
    } catch (e: DatabaseException) {
        throw DomainError.DatabaseError(e.message ?: "Unknown error")
    } catch (e: Exception) {
        throw DomainError.UnknownError(e.message ?: "Unknown error")
    }
