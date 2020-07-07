package com.hallert.voteforreddit.ui.submission

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hallert.voteforreddit.RedditApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dean.jraw.models.Submission

class SubmissionViewModel : ViewModel() {
    private var repo: SubmissionRepository = SubmissionRepository(RedditApp.database)

    val submissions: LiveData<List<Submission>> = repo.submissions.asLiveData()

    @ExperimentalCoroutinesApi
    val isLoading: LiveData<Boolean> = repo.isLoading.asLiveData()

    init {
        repo.buildSubreddit()
        startup()
    }

    fun getNextPage() {
        CoroutineScope(Main).launch {
            repo.getNextPage()
        }
    }

    fun refresh() {
        CoroutineScope(Main).launch {
            repo.refresh()
        }
    }

    fun startup() = runBlocking {
        CoroutineScope(Main).launch {
            repo.refresh()
        }
    }

    // TODO: This should be removed
    fun getSubredditName(): String {
        return repo.subredditName
    }
}


