package com.hallert.voteforreddit.ui.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hallert.voteforreddit.database.SubredditSearchEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import net.dean.jraw.models.Subreddit

class SearchViewModel @ViewModelInject constructor(
    private val searchRepository: SearchRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var currentQuery: String = "null"

    @ExperimentalCoroutinesApi
    private val searchSubChannel = ConflatedBroadcastChannel<String>()

    @ExperimentalCoroutinesApi
    @FlowPreview
    val subreddits: LiveData<List<Subreddit>> =
        searchSubChannel.asFlow().flatMapLatest { query ->
            searchRepository.getSearchSubreddits(query)
        }.asLiveData()

    @ExperimentalCoroutinesApi
    fun searchReddit(query: String) {
        currentQuery = query
        searchSubChannel.offer(currentQuery)
        CoroutineScope(Main).launch { searchRepository.searchSubreddits(currentQuery) }
    }

    fun clearNonQuery() {
        CoroutineScope(Main).launch {
            searchRepository.clearNonQuery(currentQuery)
        }
    }
}