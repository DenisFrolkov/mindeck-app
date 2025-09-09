package com.mimdeck.data.repository.util

import com.mimdeck.data.exception.DatabaseException
import com.mindeck.domain.exception.DomainError

suspend inline fun <T> handleDatabaseSuspend(crossinline block: suspend () -> T): T {
    return try {
        block()
    } catch (e: DatabaseException) {
        throw DomainError.DatabaseError(e)
    } catch (e: Exception) {
        throw DomainError.UnknownError(e)
    }
}

inline fun <T> handleDatabase(block: () -> T): T {
    return try {
        block()
    } catch (e: DatabaseException) {
        throw DomainError.DatabaseError(e)
    } catch (e: Exception) {
        throw DomainError.UnknownError(e)
    }
}