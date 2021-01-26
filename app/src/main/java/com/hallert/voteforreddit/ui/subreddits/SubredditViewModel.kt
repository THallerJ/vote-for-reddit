package com.hallert.voteforreddit.ui.subreddits

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class SubredditViewModel @ViewModelInject constructor(
    private val subredditRepository: SubredditRepository
) : ViewModel() {

    val subreddits = subredditRepository.subreddits.asLiveData()

    fun updateSubreddits() {
        CoroutineScope(Main).launch {
            subredditRepository.addSubreddits()
        }
    }
}