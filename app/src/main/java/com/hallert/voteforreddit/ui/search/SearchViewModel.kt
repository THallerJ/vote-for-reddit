package com.hallert.voteforreddit.ui.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class SearchViewModel @ViewModelInject constructor(
    private val searchRepository: SearchRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun searchReddit(query: String) {
        searchRepository.searchSubreddits(query)
    }
}