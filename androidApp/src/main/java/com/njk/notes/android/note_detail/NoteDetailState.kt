package com.njk.notes.android.note_detail

data class NoteDetailState(
    val noteTitle: String = "",
    val isNoteTitleHintFocused: Boolean = false,
    val noteContent: String  = "",
    val isNoteContentHintFocused: Boolean = false,
    val noteColor: Long = 0xFFFFFFFF,
)