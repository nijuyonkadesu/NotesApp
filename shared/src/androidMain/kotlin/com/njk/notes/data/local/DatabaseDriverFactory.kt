package com.njk.notes.data.local

import android.content.Context
import com.njk.notes.database.NoteDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

// have to follow same package structure
// Platform specific implementation of driver factory

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(NoteDatabase.Schema, context, "note.db")
    }
}