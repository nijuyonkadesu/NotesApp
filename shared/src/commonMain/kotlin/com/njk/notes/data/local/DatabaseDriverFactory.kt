package com.njk.notes.data.local

import com.squareup.sqldelight.db.SqlDriver

/**
 * Actual implementation is different on android and iOS.
 * expect says, there should by actual implementation of driver factory in platform specific ways
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver // function must be implemented in iOS and android side
}