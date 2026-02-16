package com.mindeck.domain.exception

sealed class DomainError : Exception() {
    class DatabaseError(
        override val message: String,
    ) : DomainError()

    class UnknownError(
        override val message: String,
    ) : DomainError()

    class NameAlreadyExists(
        override val message: String,
    ) : DomainError()
}
