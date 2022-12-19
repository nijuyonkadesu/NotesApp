package com.njk.notes.domain.note

/* Similar to Dao in ROOM database, through which we primarily interact with database */
interface NoteDataStore {
    suspend fun getAllNotes(): List<Note> // Flow<List<Note>> is possible. But overhead occur with iOS. Issues will happen with coroutines & dispatchers. Overkill
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note)
    suspend fun deleteNoteById(id: Long)
}
// Implementation of NoteDataStore is found in data layer
// In more complex projects, separate by features, rather than by layers.
// This app has just one feature, so it is overkill to make feature package