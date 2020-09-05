package com.hallert.voteforreddit.ui.submission

import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import com.hallert.voteforreddit.database.SubmissionDao
import com.hallert.voteforreddit.database.SubmissionEntity
import com.hallert.voteforreddit.util.WebUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import net.dean.jraw.models.Submission
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.models.TimePeriod
import net.dean.jraw.oauth.AccountHelper
import net.dean.jraw.pagination.DefaultPaginator
import java.util.*

class SubmissionRepository(
    private val submissionDao: SubmissionDao,
    private val accountHelper: AccountHelper
) {
    private lateinit var subreddit: DefaultPaginator<Submission>

    val submissions: Flow<List<Submission>>
        get() = submissionDao.getAllSubmissions().filterNotNull()

    private lateinit var subredditName: String

    @ExperimentalCoroutinesApi
    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun buildSubreddit(subName: String) {
        subreddit = accountHelper.reddit.subreddit(subName).posts().build()
        subredditName = subName
    }

    fun buildSubreddit() {
        subreddit = accountHelper.reddit.frontPage().build()
    }

    fun sortSubreddit(
        subredditName: String,
        sort: SubredditSort,
        timePeriod: TimePeriod,
        isFrontPage: Boolean
    ) {
        subreddit = if (isFrontPage) {
            accountHelper.reddit
                .frontPage()
                .sorting(sort)
                .timePeriod(timePeriod)
                .build()
        } else {
            accountHelper.reddit
                .subreddit(subredditName)
                .posts()
                .sorting(sort)
                .timePeriod(timePeriod)
                .build()
        }
    }

    fun sortSubreddit(subredditName: String, sort: SubredditSort, isFrontPage: Boolean) {
        subreddit = if (isFrontPage) {
            accountHelper.reddit
                .frontPage()
                .sorting(sort)
                .build()
        } else {
            accountHelper.reddit
                .subreddit(subredditName)
                .posts()
                .sorting(sort)
                .build()
        }
    }

    @ExperimentalCoroutinesApi
    fun getNextPage() {
        isLoading.value = true

        CoroutineScope(IO).launch {
            if (WebUtil.isOnline()) {
                val subs = subreddit.next()
                insertSubmissions(subs)
            }

            isLoading.value = false
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

        submissionDao.insertSubmissions(submissionList.toList())
    }

    @ExperimentalCoroutinesApi
    fun refresh() {
        isLoading.value = true

        CoroutineScope(IO).launch {
            if (WebUtil.isOnline()) {
                subreddit.restart()
                submissionDao.clearDatabase()
                insertSubmissions(subreddit.next())
                isLoading.value = false
            } else {
                isLoading.value = false
            }
        }
    }

    fun updateSubmission(submission: Submission) {
        CoroutineScope(IO).launch {
            submissionDao.updateSubmission(submission, submission.id)
        }
    }

    @ExperimentalCoroutinesApi
    fun switchSubreddit(subredditName: String) {
        isLoading.value = true

        CoroutineScope(IO).launch {
            if (WebUtil.isOnline()) {
                submissionDao.clearDatabase()
                buildSubreddit(subredditName)
                insertSubmissions(subreddit.next())
            }

            isLoading.value = false
        }
    }

    @ExperimentalCoroutinesApi
    fun switchSubreddit(subredditName: String, isDefault: Boolean) {
        if ((subredditName.toLowerCase(Locale.ROOT).replace("\\s".toRegex(), " ") ==
                    RedditApp.appContext.getString(R.string.frontpage).toLowerCase(Locale.ROOT))
        ) {
            isLoading.value = true

            CoroutineScope(IO).launch {
                if (WebUtil.isOnline()) {
                    submissionDao.clearDatabase()
                    buildSubreddit()
                    insertSubmissions(subreddit.next())
                }

                isLoading.value = false
            }
        } else {
            switchSubreddit(subredditName)
        }
    }
}
