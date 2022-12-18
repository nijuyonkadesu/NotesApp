package com.njk.notes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform