package com.hallert.voteforreddit.ui.submission

import com.hallert.voteforreddit.RedditApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import net.dean.jraw.models.Listing
import net.dean.jraw.models.Submission
import net.dean.jraw.pagination.DefaultPaginator

class SubmissionRepository {
    private lateinit var subreddit: DefaultPaginator<Submission>
    lateinit var subredditName: String

    fun buildSubreddit(subName: String) {
        subreddit = RedditApp.accountHelper.reddit.subreddit(subName).posts().build()
        subredditName = subName
    }

    fun buildSubreddit() {
        subreddit = RedditApp.accountHelper.reddit.frontPage().build()
        subredditName = "frontpage"
    }

    suspend fun refresh(): Listing<Submission> {
        val result = CoroutineScope(Main).async {
            subreddit.restart()

            withContext(IO) {
                subreddit.next()
            }
        }

        return result.await()
    }

    suspend fun getNextPage(): Listing<Submission> {
        val result = CoroutineScope(IO).async {
            subreddit.next()
        }

        return result.await()
    }
}
