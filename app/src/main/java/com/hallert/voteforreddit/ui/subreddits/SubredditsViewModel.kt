package com.hallert.voteforreddit.ui.subreddits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hallert.voteforreddit.RedditApp
import com.hallert.voteforreddit.ui.submission.SubmissionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.dean.jraw.models.Subreddit
import net.dean.jraw.pagination.Paginator

class SubredditsViewModel : ViewModel() {
    private var repo: SubredditsRepository = SubredditsRepository(RedditApp.database)

    val subreddits: LiveData<List<Subreddit>> = repo.subreddits.asLiveData()

    fun updateSubreddits() {
        CoroutineScope(Main).launch {
          repo.addSubreddits()
        }
    }
}