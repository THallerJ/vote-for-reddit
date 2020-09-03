package com.hallert.voteforreddit.ui.subreddits

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import net.dean.jraw.models.Subreddit

class SubredditsViewModel @ViewModelInject constructor(
    private val subredditsRepository: SubredditsRepository
) : ViewModel() {

    val subreddits: LiveData<List<Subreddit>> = subredditsRepository.subreddits.asLiveData()

    fun updateSubreddits() {
        CoroutineScope(Main).launch {
            subredditsRepository.addSubreddits()
        }
    }
}