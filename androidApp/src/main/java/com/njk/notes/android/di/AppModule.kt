package com.njk.notes.android.di

import android.app.Application
import com.njk.notes.data.local.DatabaseDriverFactory
import com.njk.notes.data.note.SqlDelightNoteDataSource
import com.njk.notes.database.NoteDatabase
import com.njk.notes.domain.note.NoteDataSource
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // SQL Android Driver
    @Provides
    @Singleton
    fun provideSqlDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).createDriver()
    }

    // NoteDataSource (like Dao in ROOM)
    @Provides
    @Singleton
    fun provideNoteDataSource(driver: SqlDriver): NoteDataSource {
        return SqlDelightNoteDataSource(NoteDatabase(driver))
    }
}