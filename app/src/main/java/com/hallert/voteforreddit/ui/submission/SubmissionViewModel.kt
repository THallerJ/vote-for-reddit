package com.hallert.voteforreddit.ui.submission

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.dean.jraw.models.Submission

class SubmissionViewModel : ViewModel() {
    val submissions = MutableLiveData<List<Submission>>()

    private var repo: SubmissionRepository = SubmissionRepository()

    init {
        repo.buildSubreddit()
    }

    fun getSubmissions() {
       submissions.value = repo.getNextSubmissions()
    }
}

