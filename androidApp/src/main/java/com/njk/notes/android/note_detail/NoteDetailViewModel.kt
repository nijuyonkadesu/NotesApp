package com.njk.notes.android.note_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.njk.notes.domain.note.Note
import com.njk.notes.domain.note.NoteDataSource
import com.njk.notes.domain.time.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
// 1 41 17
    private val noteTitle = savedStateHandle.getStateFlow("noteTitle", initialValue = "")
    private val isNoteTitleTextFocused = savedStateHandle.getStateFlow("isNoteTitleTextFocused", initialValue = false)
    private val noteContent = savedStateHandle.getStateFlow("noteContent", initialValue = "")
    private val isNoteContentTextFocused = savedStateHandle.getStateFlow("isNoteContentTextFocused", initialValue = false)
    private val noteColor = savedStateHandle.getStateFlow("noteColor", initialValue = Note.generateColors())

    val state = combine(
        noteTitle,
        isNoteTitleTextFocused,
        noteContent,
        isNoteContentTextFocused,
        noteColor
    ) { title, isTitleFocused, content, isContentFocused, color ->
        NoteDetailState(
            noteTitle = title,
            isNoteTitleHintFocused = title.isEmpty() && !isTitleFocused,
            noteContent = content,
            isNoteContentHintFocused = content.isEmpty() && !isContentFocused,
            noteColor = color,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteDetailState())

    private val _hasNoteBeenSaved = MutableStateFlow(false)
    val hasNoteBeenSaved = _hasNoteBeenSaved.asStateFlow()

    private var existingNoteId: Long? = null

    init {
        savedStateHandle.get<Long>("noteId").let {
            if(it == -1L){
                return@let
            }
            existingNoteId = it
            viewModelScope.launch {
                existingNoteId?.let { id ->
                    noteDataSource.getNoteById(id)?.let {
                        savedStateHandle["noteTitle"] = it.title
                        savedStateHandle["noteContent"] = it.content
                        savedStateHandle["noteColor"] = it.colorHex
                    }
                }
            }
        }
    }

    fun onNoteTitleChanged(text: String) {
        savedStateHandle["noteTitle"] = text
    }

    fun onNoteContentChanged(text: String) {
        savedStateHandle["noteContent"] = text
    }

    fun onNoteTitleFocusChanged(isFocused: Boolean) {
        savedStateHandle["isNoteTitleFocused"] = isFocused
    }

    fun onNoteContentFocusChanged(isFocused: Boolean) {
        savedStateHandle["isNoteContentFocused"] = isFocused
    }

    fun saveNote() {
        viewModelScope.launch {
            noteDataSource.insertNote(
                Note(
                    id = existingNoteId,
                    title = noteTitle.value,
                    content = noteContent.value,
                    colorHex = noteColor.value,
                    created = DateTimeUtil.now()
                )
            )
            _hasNoteBeenSaved.value = true // to observe and pop backstack
        }
    }

}