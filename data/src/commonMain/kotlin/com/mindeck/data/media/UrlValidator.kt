package com.mindeck.data.media

import io.ktor.http.URLProtocol
import io.ktor.http.Url

object UrlValidator {
    private val allowedSchemes = setOf(URLProtocol.HTTP.name, URLProtocol.HTTPS.name)
    private val blockedHostnames = setOf("localhost")

    fun isSafe(url: String): Boolean {
        val parsed = runCatching { Url(url) }.getOrNull() ?: return false
        val host = parsed.host

        if (parsed.protocol.name !in allowedSchemes) return false
        if (host in blockedHostnames || host.endsWith(".local")) return false

        val octets = parseIpv4(host) ?: return true
        return !isPrivateOrReserved(octets)
    }

    private fun parseIpv4(host: String): IntArray? {
        val parts = host.split(".")
        if (parts.size != 4) return null
        return runCatching {
            IntArray(4) { i -> parts[i].toInt().also { require(it in 0..255) } }
        }.getOrNull()
    }

    private fun isPrivateOrReserved(octets: IntArray): Boolean {
        val a = octets[0]
        val b = octets[1]
        return a == 10 ||
            a == 127 ||
            (a == 172 && b in 16..31) ||
            (a == 192 && b == 168) ||
            (a == 169 && b == 254)
    }
}
