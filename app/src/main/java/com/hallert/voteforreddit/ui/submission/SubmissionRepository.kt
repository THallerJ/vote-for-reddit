package com.hallert.voteforreddit.ui.submission

import androidx.lifecycle.MutableLiveData
import com.hallert.voteforreddit.RedditApp
import com.hallert.voteforreddit.database.RedditDatabase
import com.hallert.voteforreddit.database.SubmissionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import net.dean.jraw.models.Submission
import net.dean.jraw.pagination.DefaultPaginator

class SubmissionRepository(private val database: RedditDatabase) {
    private lateinit var subreddit: DefaultPaginator<Submission> // TODO: This should be removed
    lateinit var subredditName: String // TODO: This should be removed

    val submissions: Flow<List<Submission>>
        get() = database.submissionDao.getAllSubmissions()


    // TODO: Remove buildSubreddit methods
    fun buildSubreddit(subName: String) {
        subreddit =
            RedditApp.accountHelper.reddit.subreddit(subName).posts().build() // This should be passed into the methods as needed
        subredditName = subName
    }

    fun buildSubreddit() {
        subreddit = RedditApp.accountHelper.reddit.frontPage().build() // This should be passed into the methods as needed
        subredditName = "frontpage"
    }

    fun getNextPage() {
        CoroutineScope(IO).launch {
            insertSubmissions(subreddit.next())
        }
    }

    private fun insertSubmissions(submissions: List<Submission>) {
        val submissionList = mutableListOf<SubmissionEntity>()

        for (submission in submissions) {
            val entity = SubmissionEntity(
                submission.id,
                submission,
                System.currentTimeMillis()
            )

            submissionList.add(entity)
        }

        database.submissionDao.insertSubmissions(submissionList.toList())
    }

    fun refresh() {
        CoroutineScope(IO).launch {
            subreddit.restart()
            database.submissionDao.clearDatabase()
            insertSubmissions(subreddit.next())
        }
    }
}
