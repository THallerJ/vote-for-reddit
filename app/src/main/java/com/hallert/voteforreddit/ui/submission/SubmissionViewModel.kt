package com.hallert.voteforreddit.ui.submission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hallert.voteforreddit.RedditApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dean.jraw.models.Submission

class SubmissionViewModel : ViewModel() {
    private var repo: SubmissionRepository = SubmissionRepository(RedditApp.database)

    val submissions: LiveData<List<Submission>> = repo.submissions.asLiveData()

    val isLoading = MutableLiveData<Boolean>()


    init {
        repo.buildSubreddit()
        startup()
    }

    fun getNextPage() {
        isLoading.value = true
        CoroutineScope(Main).launch {
            repo.getNextPage()
            isLoading.value = false
        }
    }

    fun refresh() {
        isLoading.value = true
        CoroutineScope(Main).launch {
            repo.refresh()
            isLoading.value = false
        }
    }

    // This ensures that the database is cleared before creating the fragment
    // however, this also blocks the refresh animation
    fun startup() = runBlocking{
        isLoading.value = true
        CoroutineScope(Main).launch {
            repo.refresh()
            isLoading.value = false
        }
    }

    // TODO: This should be removed
    fun getSubredditName(): String {
        return repo.subredditName
    }
}


