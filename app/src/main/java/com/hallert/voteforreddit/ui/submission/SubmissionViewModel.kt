package com.hallert.voteforreddit.ui.submission

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hallert.voteforreddit.R
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dean.jraw.models.Submission

class SubmissionViewModel @ViewModelInject constructor(
    private val submissionRepository: SubmissionRepository,
    @ActivityContext private val context: Context,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val submissions: LiveData<List<Submission>> = submissionRepository.submissions.asLiveData()

    @ExperimentalCoroutinesApi
    val isLoading: LiveData<Boolean> = submissionRepository.isLoading.asLiveData()

    init {
        submissionRepository.buildSubreddit()
        startup()
    }

    fun getNextPage() {
        CoroutineScope(Main).launch {
            submissionRepository.getNextPage()
        }
    }

    fun refresh() {
        CoroutineScope(Main).launch {
            submissionRepository.refresh()
        }
    }

    @ExperimentalCoroutinesApi
    fun startup() = runBlocking {
        CoroutineScope(Main).launch {
            submissionRepository.refresh()
        }
    }


    @ExperimentalCoroutinesApi
    fun switchSubreddits(subredditName: String) {
        CoroutineScope(Main).launch {
            submissionRepository.switchSubreddit(subredditName)
        }
    }

    @ExperimentalCoroutinesApi
    fun switchFrontpage() {
        submissionRepository.switchSubreddit(context.getString(R.string.frontpage), true)
    }
}


