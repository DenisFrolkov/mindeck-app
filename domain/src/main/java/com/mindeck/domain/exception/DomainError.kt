package com.mindeck.domain.exception

sealed class DomainError(message: String, cause: Throwable? = null) : Throwable(message, cause) {
    class DatabaseError(cause: Throwable? = null) : DomainError("Database operation failed", cause)
    class UnknownError(cause: Throwable? = null) : DomainError("Unknown error", cause)
}