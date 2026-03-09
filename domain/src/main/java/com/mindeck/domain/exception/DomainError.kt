package com.mindeck.domain.exception

sealed class DomainError : Exception() {
    object DatabaseError : DomainError()

    object NameAlreadyExists : DomainError()
}
