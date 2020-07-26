package com.hallert.voteforreddit.ui.subreddits

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import net.dean.jraw.models.Subreddit

class SubredditsViewModel @ViewModelInject constructor(
    private val subredditsRepository: SubredditsRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ViewModel() {

    val subreddits: LiveData<List<Subreddit>> = subredditsRepository.subreddits.asLiveData()

    fun updateSubreddits() {
        CoroutineScope(Main).launch {
            subredditsRepository.addSubreddits()
        }
    }
}