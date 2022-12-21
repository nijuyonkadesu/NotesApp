package com.njk.notes.android.note_list

import com.njk.notes.domain.note.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val searchString: String = "",
    val isSearchActive: Boolean = false
)
