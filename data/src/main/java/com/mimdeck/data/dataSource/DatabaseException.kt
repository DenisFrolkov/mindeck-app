package com.mimdeck.data.dataSource

class DatabaseException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)