package com.hallert.voteforreddit.ui.submission

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dean.jraw.models.Submission
import net.dean.jraw.models.VoteDirection
import net.dean.jraw.oauth.AccountHelper
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SubmissionViewModel @ViewModelInject constructor(
    private val submissionRepository: SubmissionRepository,
    private val accountHelper: AccountHelper,
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
        submissionRepository.switchSubreddit(
            RedditApp.appContext.getString(R.string.frontpage),
        true
        )
    }

    fun voteSubmission(submission: Submission, voteDirection: VoteDirection) {
        CoroutineScope(IO).launch {
            val votedSubmission = accountHelper.reddit.submission(submission.id)

            when (voteDirection) {
                VoteDirection.UP -> {
                    if (submission.vote == voteDirection) {
                        votedSubmission.unvote()
                    } else {
                        votedSubmission.upvote()
                    }
                }
                VoteDirection.DOWN -> {
                    if (submission.vote == voteDirection) {
                        votedSubmission.unvote()
                    } else {
                        votedSubmission.downvote()
                    }
                }
                VoteDirection.NONE -> {
                    votedSubmission.unvote()
                }
            }

            submissionRepository.updateSubmission(votedSubmission.inspect())
        }
    }
}


