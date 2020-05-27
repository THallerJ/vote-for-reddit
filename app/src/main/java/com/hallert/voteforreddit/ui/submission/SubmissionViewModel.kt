package com.hallert.voteforreddit.ui.submission

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import net.dean.jraw.models.Submission

class SubmissionViewModel : ViewModel() {
    val submissions = MutableLiveData<MutableList<Submission>>()

    private var repo: SubmissionRepository = SubmissionRepository()

    init {
        repo.buildSubreddit()
    }

    fun getSubmissions() {
        CoroutineScope(Main).launch { submissions.value = repo.getNextPage() }
    }

    fun getSubredditName(): String {
        return repo.subredditName
    }

    fun refresh() {
        CoroutineScope(Main).launch { submissions.value = repo.refresh() }
    }
}

