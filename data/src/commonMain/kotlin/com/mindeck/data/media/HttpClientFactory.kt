package com.mindeck.data.media

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout

fun createHttpClient(): HttpClient =
    HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
            connectTimeoutMillis = 5_000
            socketTimeoutMillis = 10_000
        }
    }
