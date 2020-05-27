package com.hallert.voteforreddit.ui.submission

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import net.dean.jraw.models.Submission

class SubmissionViewModel : ViewModel() {
    val submissions = MutableLiveData<MutableList<Submission>>()
    val isLoading = MutableLiveData<Boolean>()

    private var repo: SubmissionRepository = SubmissionRepository()

    init {
        repo.buildSubreddit()
    }

    fun getSubmissions() {
        isLoading.value = true
        CoroutineScope(Main).launch {
            submissions.value = repo.getNextPage()
            isLoading.value = false
        }
    }

    fun getSubredditName(): String {
        return repo.subredditName
    }

    fun refresh() {
        isLoading.value = true
        CoroutineScope(Main).launch {
            submissions.value = repo.refresh()
            isLoading.value = false
        }
    }
}

