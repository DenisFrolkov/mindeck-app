package com.mimdeck.data.util

import com.mimdeck.data.exception.DatabaseException
import com.mindeck.domain.exception.DomainError

suspend inline fun <T> handleDatabaseSuspend(crossinline block: suspend () -> T): T {
    return try {
        block()
    } catch (e: DatabaseException) {
        throw DomainError.DatabaseError(e)
    }
}

inline fun <T> handleDatabase(crossinline block: () -> T): T {
    return try {
        block()
    } catch (e: DatabaseException) {
        throw DomainError.DatabaseError(e)
    }
}