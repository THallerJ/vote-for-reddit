package com.hallert.voteforreddit.ui.submission

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hallert.voteforreddit.RedditApp
import net.dean.jraw.models.Submission

class SubmissionViewModel : ViewModel() {
    val submissions = MutableLiveData<MutableList<Submission>>()

    private var repo: SubmissionRepository = SubmissionRepository()

    init {
       repo.buildSubreddit()
    }

    fun getSubmissions() {
        // TODO: implement caching, this should allow the listt to continue scrolling
       submissions.value = repo.getNextSubmissions()
    }
}

