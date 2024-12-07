package com.mimdeck.data.exception

class DatabaseException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)