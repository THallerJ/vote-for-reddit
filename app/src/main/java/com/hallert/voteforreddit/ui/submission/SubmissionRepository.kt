package com.hallert.voteforreddit.ui.submission

import com.hallert.voteforreddit.RedditApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import net.dean.jraw.models.Listing
import net.dean.jraw.models.Submission
import net.dean.jraw.pagination.DefaultPaginator

// TODO: Subreddits fragment may need to know about this class
class SubmissionRepository {
    private lateinit var subreddit: DefaultPaginator<Submission>

    fun buildSubreddit(subredditName: String) {
        subreddit = RedditApp.accountHelper.reddit.subreddit(subredditName).posts().build()
    }

    fun buildSubreddit() {
        subreddit = RedditApp.accountHelper.reddit.frontPage().build()
    }

    suspend fun queryAPI(): Listing<Submission> {
        val result = CoroutineScope(IO).async {
            subreddit.next()
        }

        return result.await()
    }

    fun getNextSubmissions(): Listing<Submission> {
        return runBlocking {
            queryAPI()
        }
    }
}
