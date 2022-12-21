package com.njk.notes.data.note

import com.njk.notes.database.NoteDatabase
import com.njk.notes.domain.note.Note
import com.njk.notes.domain.note.NoteDataSource
import com.njk.notes.domain.time.DateTimeUtil

class SqlDelightNoteDataSource(db: NoteDatabase): NoteDataSource {

    /**
     *  List of queries defined in sqldelight > database > note.sq
     */
    private val queries = db.noteQueries
    override suspend fun getAllNotes(): List<Note> {
        return queries
            .getAllNotes()
            .executeAsList()
            .map { it.toNote() }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return queries
            .getNoteById(id)
            .executeAsOneOrNull()
            ?.toNote()
    }

    override suspend fun insertNote(note: Note) {
        queries.insertNote(
            id = note.id,
            title = note.title,
            content = note.content,
            colorHex = note.colorHex,
            created = DateTimeUtil.toEpochMillis(note.created) /* it is Integer, as per definition in note.sq */
        )
    }

    override suspend fun deleteNoteById(id: Long) {
        queries.deleteNoteById(id)
    }
}