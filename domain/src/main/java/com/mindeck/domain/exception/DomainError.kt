package com.mindeck.domain.exception

sealed class DomainError : Exception() {
    class DatabaseError : DomainError()

    class NameAlreadyExists : DomainError()
}
