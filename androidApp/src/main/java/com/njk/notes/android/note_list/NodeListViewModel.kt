package com.njk.notes.android.note_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.njk.notes.domain.note.Note
import com.njk.notes.domain.note.NoteDataSource
import com.njk.notes.domain.note.SearchNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NodeListViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val searchNotes = SearchNotes() // if it had parameters, Hilt would be used

    private val notes = savedStateHandle.getStateFlow("notes", emptyList<Note>())
    private val searchText = savedStateHandle.getStateFlow("searchText", "")
    private val isSearchActive = savedStateHandle.getStateFlow("isSearchActive", false)
    // Reason for not restoring the whole class is, not every value present in it is necessary
    // if you still want, have to make the class parcelable and put in saved State Handle

    val state = combine(notes, searchText, isSearchActive) { notes, searchText, isSearchActive ->
        NoteListState(
            notes = searchNotes.execute(notes, searchText),
            searchText = searchText,
            isSearchActive = isSearchActive
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteListState())
    // Only runs stateIn when it is necessary

    fun loadNotes(){
        viewModelScope.launch {
            savedStateHandle["notes"] = noteDataSource.getAllNotes()
        }
    }
    fun onSearchTextChange(text: String){
        savedStateHandle["searchText"] = text
    }
    fun onToggleSearch(){
        savedStateHandle["isSearchActive"] = !isSearchActive.value
        if(!isSearchActive.value){
            savedStateHandle["searchText"] = "" // reset search text if user closes search bar
        }
    }
    fun deleteNoteById(id: Long){
        viewModelScope.launch {
            noteDataSource.deleteNoteById(id)
            loadNotes() // Since we're not using Flow, we have to load it manually
                        // We have flow for the STATE, not for the items in the DataBase
        }
    }
}