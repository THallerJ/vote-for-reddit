package com.hallert.voteforreddit.ui.submission

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dean.jraw.models.Submission
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.models.TimePeriod
import net.dean.jraw.models.VoteDirection
import net.dean.jraw.oauth.AccountHelper
import timber.log.Timber

@ExperimentalCoroutinesApi
class SubmissionViewModel @ViewModelInject constructor(
    private val submissionRepository: SubmissionRepository,
    private val accountHelper: AccountHelper,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val submissions: LiveData<List<Submission>> = submissionRepository.submissions.asLiveData()

    @ExperimentalCoroutinesApi
    val isLoading: LiveData<Boolean> = submissionRepository.isLoading.asLiveData()

    private var isFrontPage = true
    private var subredditName: String

    init {
        subredditName = RedditApp.appContext.getString(R.string.frontpage)
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
        isFrontPage = false
        this.subredditName = subredditName

        CoroutineScope(Main).launch {
            submissionRepository.switchSubreddit(subredditName)
        }
    }

    @ExperimentalCoroutinesApi
    fun switchFrontpage() {
        isFrontPage = true
        subredditName = RedditApp.appContext.getString(R.string.frontpage)

        submissionRepository.switchSubreddit(
            subredditName,
            true
        )
    }

    fun changeSort(sort: SubredditSort, timePeriod: TimePeriod?) {
        if (timePeriod == null) {
            submissionRepository.sortSubreddit(subredditName, sort, isFrontPage)
        } else {
            submissionRepository.sortSubreddit(subredditName, sort, timePeriod, isFrontPage)
        }

        refresh()
    }

    fun voteSubmission(submission: Submission, voteDirection: VoteDirection) {
        CoroutineScope(IO).launch {
            val votedSubmission = accountHelper.reddit.submission(submission.id)

            try {
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
            } catch (e: net.dean.jraw.ApiException) {
                Timber.e(e)
            }

            submissionRepository.updateSubmission(votedSubmission.inspect())
        }
    }
}


