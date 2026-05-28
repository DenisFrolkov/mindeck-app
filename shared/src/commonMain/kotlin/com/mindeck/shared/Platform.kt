package com.mindeck.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform